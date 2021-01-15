/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.config

import com.bphenriques.billing.biller.BillFactory
import com.bphenriques.billing.biller.strategies.{CallRecordBillFactory, SeqCallRecordBillFactory}
import com.bphenriques.billing.model.CallRecord
import com.bphenriques.billing.supplier.CallRecordsFileSupplier
import com.bphenriques.billing.supplier.csv.{CSVCallRecordsFileSupplier, CSVCallRecordsFileSupplierConfig}

import java.text.DecimalFormat
import com.bphenriques.billing.biller.strategies.{CallRecordBillFactory, SeqCallRecordBillFactory}
import com.bphenriques.billing.supplier.csv.CSVCallRecordsFileSupplierConfig
import com.typesafe.config.Config

import scala.util.Try

/**
  * The Application configuration.
  *
  * @param costFormat           The format of cost.
  * @param callRecordsSupplier  The records supplier.
  * @param biller        The strategy used to bill a sequence of calls.
  */
final case class AppConfig(
  costFormat: DecimalFormat,
  callRecordsSupplier: CallRecordsFileSupplier,
  biller: BillFactory[Seq[CallRecord]]
)

/**
  * Companion class of [[AppConfig]].
  */
object AppConfig {
  import ConfigEnricher.RichConfig

  /**
    * Creates a [[AppConfig]].
    *
    * @param conf The configuration.
    * @return The instance of [[AppConfig]].
    */
  def fromConfig(conf: Config): Try[AppConfig] = for {
    costFormat <- Try(new DecimalFormat(conf.getString("cost-format")))
    callRecordsSupplierConfig <- conf.getConfig("csv").read[CSVCallRecordsFileSupplierConfig]
  } yield AppConfig(
    costFormat = costFormat,
    callRecordsSupplier = CSVCallRecordsFileSupplier.fromConfig(callRecordsSupplierConfig),
    biller = new SeqCallRecordBillFactory(new CallRecordBillFactory)
  )
}
