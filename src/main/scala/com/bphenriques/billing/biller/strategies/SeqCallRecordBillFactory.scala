/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.biller.strategies

import com.bphenriques.billing.biller.BillFactory
import com.bphenriques.billing.model.{Bill, CallRecord, Contact}
import com.bphenriques.billing.model.CallRecord
import com.typesafe.scalalogging.LazyLogging

/**
  * Generates [[Bill]] given a set of [[CallRecord]].
  *
  * @param callPricing The strategy used to bill [[CallRecord]].
  */
final class SeqCallRecordBillFactory(
  callPricing: BillFactory[CallRecord]
) extends BillFactory[Seq[CallRecord]] with LazyLogging {

  /**
    * Processes the cost of a set of [[CallRecord]] transcribed in the file.
    *
    * @param records The set of calls.
    * @return The cost formatted.
    */
  override def createBill(records: Seq[CallRecord]): Bill =
    if(records.isEmpty) {
      Bill.empty
    } else {
      logger.info("Creating the set of bill for each caller...")
      val callerToBill: Map[Contact, Bill] = records
        .groupBy(_.from)
        .mapValues(_.map(callPricing.createBill))
        .mapValues(_.foldLeft(Bill.empty) (_ + _))

      logger.debug("Obtaining the highest total call duration...")
      val (_, billWithHighestDuration) = callerToBill
        .maxBy { case (_, bill) => bill.duration }

      logger.debug(s"Contacts with duration of ${billWithHighestDuration.duration} will not pay.")
      callerToBill.values
        .filter { bill => bill.duration != billWithHighestDuration.duration }
        .foldLeft(Bill.empty)(_ + _)
    }
}
