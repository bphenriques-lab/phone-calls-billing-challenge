/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.supplier.csv

import java.io.File
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, LocalTime}

import com.talkdesk.billing.exception.BillingException
import com.talkdesk.billing.model.{CallRecord, Contact}
import com.talkdesk.billing.supplier.CallRecordsFileSupplier
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

/**
  * Provides a stream of [[com.talkdesk.billing.model.CallRecord]] from a CSV file in the following format:
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
final class CSVCallRecordsFileSupplier(
  config: CSVCallRecordsFileSupplierConfig
) extends CallRecordsFileSupplier with LazyLogging {

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
    * Decoder that converts a row to an instance of [[com.talkdesk.billing.model.CallRecord]].
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

    if (config.validateFile) {
      // Stream the file once to check for any errors. If no errors are found, return a new stream.
      reader().toStream.collectFirst { case Left(error) => error } match {
        case Some(readError) => throw BillingException("Error decoding file.", readError)
        case _               => reader().toStream.map(_.right.get)
      }
    } else {
      // Stream the file only once and log any line that is not correct.
      reader().toStream.map {
        case Right(record) => Success(record)
        case Left(error) =>
          val ex = BillingException("Error decoding line.", error)
          logger.error(ex.message)
          Failure(ex)
      }.filter(_.isSuccess).map(_.get)
    }
  }
}

/**
  * Companion object.
  */
object CSVCallRecordsFileSupplier {

  /**
    * Creates an instance of [[com.talkdesk.billing.supplier.CallRecordsFileSupplier]] that reads [[com.talkdesk.billing.model.CallRecord]] from a
    * CSV file.
    *
    * @param config The configuration.
    * @return An instance of [[com.talkdesk.billing.supplier.csv.CSVCallRecordsFileSupplier]].
    */
  def fromConfig(config: CSVCallRecordsFileSupplierConfig): CSVCallRecordsFileSupplier =
    new CSVCallRecordsFileSupplier(config)
}
