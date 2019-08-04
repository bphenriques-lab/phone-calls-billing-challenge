/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.supplier.csv

import com.typesafe.config.Config

import scala.language.implicitConversions
import scala.util.Try

/**
  * Configuration file of [[com.talkdesk.billing.supplier.csv.CSVCallRecordsFileSupplier]].
  *
  * @param separator    The values separator.
  * @param header       If the CSV file contains an header.
  */
final case class CSVCallRecordsFileSupplierConfig(
  separator: Char,
  header: Boolean,
  validateFile: Boolean
)

/**
  * Companion object of [[com.talkdesk.billing.supplier.csv.CSVCallRecordsFileSupplierConfig]].
  */
object CSVCallRecordsFileSupplierConfig {
  import com.talkdesk.billing.config.ConfigEnricher.RichConfig

  /**
    * Creates a instance of [[com.talkdesk.billing.supplier.csv.CSVCallRecordsFileSupplierConfig]] given a configuration.
    *
    * @param conf The configuration.
    * @return The instance of [[com.talkdesk.billing.supplier.csv.CSVCallRecordsFileSupplierConfig]].
    */
  implicit def fromConfig(conf: Config): Try[CSVCallRecordsFileSupplierConfig] = for {
    separator <- Try(conf.getChar("separator"))
    header <- Try(conf.getBoolean("header"))
    validateFile <- Try(conf.getBoolean("validate-file"))
  } yield CSVCallRecordsFileSupplierConfig(separator, header, validateFile)

  /**
    * Default configuration.
    */
  val Default: CSVCallRecordsFileSupplierConfig = CSVCallRecordsFileSupplierConfig(
    separator = ';',
    header    = false,
    validateFile = true
  )
}
