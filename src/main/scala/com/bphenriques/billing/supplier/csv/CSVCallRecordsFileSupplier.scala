/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.supplier.csv

import com.bphenriques.billing.exception.BillingException
import com.bphenriques.billing.model.{CallRecord, Contact}
import com.bphenriques.billing.supplier.CallRecordsFileSupplier

import java.io.File
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, LocalTime}
import com.bphenriques.billing.model.CallRecord

import scala.util.Try

/**
  * Provides a stream of [[CallRecord]] from a CSV file in the following format:
  * <ul>
  *   <li>`time_of_start`: When the call started in the format `HH:mm:ss`.</li>
  *   <li>`time_of_finish`: When the call ended in the format `HH:mm:ss`.</li>
  *   <li>`call_from`: Who started the call.</li>
  *   <li>`call_to`: To whom `call_from` is calling.</li>
  * </ul>
  * <p>
  *
  * @param config The configuration.
  * @author Bruno Henriques (brunoaphenriques@gmail.com)
  * @version 1.0.0-SNAPSHOT
  */
final class CSVCallRecordsFileSupplier(config: CSVCallRecordsFileSupplierConfig) extends CallRecordsFileSupplier {

  /**
    * The timestamp format of `time_of_start` and `time_of_finish`.
    */
  private val TimestampFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

  /**
    * Reference date.
    */
  private val ReferenceDay = LocalDate.of(2019, 1, 1)

  import kantan.csv._
  import kantan.csv.ops._

  /**
    * Decoder that converts a row to an instance of [[CallRecord]].
    */
  implicit private val decoder: RowDecoder[CallRecord] = RowDecoder.ordered {
    (start: String, end: String, from: String, to: String) => {
      val startTime = LocalTime.parse(start.trim, TimestampFormat)
      val endTime = LocalTime.parse(end.trim, TimestampFormat)

      // If the call ended after midnight, then we consider that `time_of_finish` occurred in the next day.
      val startDateTime = LocalDateTime.of(ReferenceDay, LocalTime.parse(start.trim, TimestampFormat))
      val endDateTime = endTime match {
        case time if time.isBefore(startTime) => LocalDateTime.of(ReferenceDay.plusDays(1), endTime)
        case time                             => LocalDateTime.of(ReferenceDay, time)
      }

      CallRecord(startDateTime, endDateTime, Contact(from.trim), Contact(to.trim))
    }
  }

  /**
    * The CSV configuration.
    */
  private val csvConfiguration = rfc
    .withHeader(config.header)
    .withCellSeparator(config.separator)

  /**
    * @inheritdoc
    *
    * @note Given that we are streaming, any errors are thrown when evaluating the line.
    * @todo Optionally validate the file before returning the stream.
    */
  override def from(file: File): Try[Seq[CallRecord]] = Try {
    require(file.isFile && file.exists(), s"The provided file ${file.getAbsolutePath} does not exist.")

    /**
      * Auxiliary method to re-read the file.
      */
    def reader(): CsvReader[ReadResult[CallRecord]] = file.asCsvReader[CallRecord](csvConfiguration)

    // UPDATE: After couple of years... we are better off logging the errors outs and compensate any difference.
    // As it is, we are reading the files twice. Is not bad per say.. but something to consider.
    //
    // Stream the file once to check for any errors. If no errors are found, return a new stream.
    // This leads to iterating the file twice, however for this use-case, it is cleaner than returning
    // Seq[Try[CallRecord]] that requires checking for errors line-by-line downstream.
    reader().toStream.collectFirst { case Left(error) => error } match {
      case Some(readError) => throw BillingException("Error decoding file.", readError)
      case _               => reader().toStream.map(_.right.get)
    }
  }
}

/**
  * Companion object.
  */
object CSVCallRecordsFileSupplier {

  /**
    * Creates an instance of [[CallRecordsFileSupplier]] that reads [[CallRecord]] from a
    * CSV file.
    *
    * @param config The configuration.
    * @return An instance of [[CSVCallRecordsFileSupplier]].
    */
  def fromConfig(config: CSVCallRecordsFileSupplierConfig): CSVCallRecordsFileSupplier =
    new CSVCallRecordsFileSupplier(config)
}
