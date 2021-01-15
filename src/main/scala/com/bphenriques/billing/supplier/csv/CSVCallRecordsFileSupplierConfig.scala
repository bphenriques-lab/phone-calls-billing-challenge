/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.supplier.csv

import com.typesafe.config.Config

import scala.language.implicitConversions
import scala.util.Try

/**
  * Configuration file of [[CSVCallRecordsFileSupplier]].
  *
  * @param separator    The values separator.
  * @param header       If the CSV file contains an header.
  */
final case class CSVCallRecordsFileSupplierConfig(
  separator: Char,
  header: Boolean
)

/**
  * Companion object of [[CSVCallRecordsFileSupplierConfig]].
  */
object CSVCallRecordsFileSupplierConfig {
  import com.bphenriques.billing.config.ConfigEnricher.RichConfig

  /**
    * Creates a instance of [[CSVCallRecordsFileSupplierConfig]] given a configuration.
    *
    * @param conf The configuration.
    * @return The instance of [[CSVCallRecordsFileSupplierConfig]].
    */
  implicit def fromConfig(conf: Config): Try[CSVCallRecordsFileSupplierConfig] = for {
    separator <- Try(conf.getChar("separator"))
    header <- Try(conf.getBoolean("header"))
  } yield CSVCallRecordsFileSupplierConfig(separator, header)

  /**
    * Default configuration.
    */
  val Default: CSVCallRecordsFileSupplierConfig = CSVCallRecordsFileSupplierConfig(
    separator = ';',
    header    = false
  )
}
