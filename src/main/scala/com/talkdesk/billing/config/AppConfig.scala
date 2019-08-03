/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.config

import java.text.DecimalFormat

import com.talkdesk.billing.biller.Biller
import com.talkdesk.billing.biller.strategies.{MultipleCallsBiller, SingleCallBiller}
import com.talkdesk.billing.model.CallRecord
import com.talkdesk.billing.supplier.CallRecordsFileSupplier
import com.talkdesk.billing.supplier.csv.{CSVCallRecordsFileSupplier, CSVCallRecordsFileSupplierConfig}
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
  biller: Biller[Seq[CallRecord]]
)

/**
  * Companion class of [[com.talkdesk.billing.config.AppConfig]].
  */
object AppConfig {
  import com.talkdesk.billing.config.ConfigEnricher.RichConfig

  /**
    * Creates a [[com.talkdesk.billing.config.AppConfig]].
    *
    * @param conf The configuration.
    * @return The instance of [[com.talkdesk.billing.config.AppConfig]].
    */
  def fromConfig(conf: Config): Try[AppConfig] = for {
    costFormat <- Try(new DecimalFormat(conf.getString("cost-format")))
    callRecordsSupplierConfig <- conf.getConfig("csv").read[CSVCallRecordsFileSupplierConfig]
  } yield AppConfig(
    costFormat = costFormat,
    callRecordsSupplier = CSVCallRecordsFileSupplier.fromConfig(callRecordsSupplierConfig),
    biller = new MultipleCallsBiller(new SingleCallBiller)
  )
}
