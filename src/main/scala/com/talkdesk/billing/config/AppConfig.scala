/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.config

import com.talkdesk.billing.manager.SeqCallsBillGenerator
import com.talkdesk.billing.manager.Types.CostFormat
import com.talkdesk.billing.manager.generator.{BaseCallBillGenerator, BaseSeqCallsBillGenerator}
import com.talkdesk.billing.supplier.CallRecordsFileSupplier
import com.talkdesk.billing.supplier.csv.{CSVCallRecordsFileSupplier, CSVCallRecordsFileSupplierConfig}
import com.typesafe.config.Config

import scala.util.Try

/**
  * The Application configuration.
  *
  * @param costFormat           The format of cost.
  * @param callRecordsSupplier  The records supplier.
  * @param billGenerator        The strategy used to bill a sequence of calls.
  */
final case class AppConfig(
  costFormat: CostFormat,
  callRecordsSupplier: CallRecordsFileSupplier,
  billGenerator: SeqCallsBillGenerator
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
    costFormat <- Try(new CostFormat(conf.getString("cost-format")))
    callRecordsSupplierConfig <- conf.getConfig("csv").read[CSVCallRecordsFileSupplierConfig]
  } yield AppConfig(
    costFormat = costFormat,
    callRecordsSupplier = CSVCallRecordsFileSupplier.fromConfig(callRecordsSupplierConfig),
    billGenerator = new BaseSeqCallsBillGenerator(new BaseCallBillGenerator)
  )
}
