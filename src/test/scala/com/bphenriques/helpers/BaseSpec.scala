/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.helpers

import com.bphenriques.billing.model.Bill
import com.bphenriques.billing.supplier.CallRecordsFileSupplier
import com.bphenriques.billing.supplier.csv.{CSVCallRecordsFileSupplier, CSVCallRecordsFileSupplierConfig}

import java.io.File
import java.text.DecimalFormat
import java.time.{LocalDate, LocalDateTime, LocalTime}
import com.bphenriques.billing.supplier.csv.CSVCallRecordsFileSupplierConfig
import org.scalatest.{FlatSpec, Matchers}

/**
  * Provides boiler plate code to streamline unit tests.
  */
class BaseSpec extends FlatSpec with Matchers {

  /**
    * Default format.
    */
  val DefaultCostFormat: DecimalFormat = Bill.DefaultFormat

  /**
    * Default [[CallRecordsFileSupplier]].
    */
  val DefaultRecordsSupplier: CallRecordsFileSupplier = CSVCallRecordsFileSupplier.fromConfig(
    CSVCallRecordsFileSupplierConfig.Default
  )

  /**
    * Reference date to construct instances of [[LocalDateTime]].
    */
  val ReferenceDate: LocalDate = LocalDate.of(2019, 1, 1)

  /**
    * Noon LocalDateTime (12:00) using the reference date.
    */
  val Noon: LocalDateTime = LocalDateTime.of(ReferenceDate, LocalTime.NOON)

  /**
    * Midnight LocalDateTime (00:00) using the reference date.
    */
  val Midnight: LocalDateTime = LocalDateTime.of(ReferenceDate, LocalTime.MIDNIGHT)

  /**
    * Folder that contains solely valid samples.
    */
  val ValidSamplesFolder: File = new File(getClass.getClassLoader.getResource("valid-samples").getFile)

  /**
    * Folder that contains solely invalid samples.
    */
  val InvalidSamplesFolder: File = new File(getClass.getClassLoader.getResource("invalid-samples").getFile)

  /**
    * Gets a file within [[ValidSamplesFolder]].
    *
    * @param file The name of the file.
    * @return The file.
    */
  def getValidResource(file: String): File = new File(ValidSamplesFolder, file)

  /**
    * Gets a file within [[InvalidSamplesFolder]].
    *
    * @param file The name of the file.
    * @return The file.
    */
  def getInvalidResource(file: String): File = new File(InvalidSamplesFolder, file)

  /**
    * Returns a [[LocalDateTime]] given a [[LocalTime]].
    *
    * @param time The time.
    * @return The [[LocalDateTime]].
    */
  def localDataTimeAt(time: LocalTime): LocalDateTime = LocalDateTime.of(ReferenceDate, time)

  /**
    * Returns a [[LocalDateTime]] given a hour, minute and a second.
    *
    * @param hour   the hour-of-day to represent, from 0 to 23.
    * @param minute the minute-of-hour to represent, from 0 to 59.
    * @param second the second-of-minute to represent, from 0 to 59.
    * @return
    */
  def localDataTimeAt(hour: Int, minute: Int, second: Int): LocalDateTime =
    localDataTimeAt(LocalTime.of(hour, minute, second))
}
