/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager.generator

import com.talkdesk.billing.manager.{Bill, CallBillGenerator, SeqCallsBillGenerator}
import com.talkdesk.billing.model.{CallRecord, Contact}
import com.typesafe.scalalogging.LazyLogging

/**
  * Generates [[com.talkdesk.billing.manager.Bill]] given a set of [[com.talkdesk.billing.model.CallRecord]].
  *
  * @param callPricing The strategy used to bill [[com.talkdesk.billing.model.CallRecord]].
  */
final class BaseSeqCallsBillGenerator(callPricing: CallBillGenerator) extends SeqCallsBillGenerator with LazyLogging {

  /**
    * Processes the cost of a set of [[com.talkdesk.billing.model.CallRecord]] transcribed in the file.
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
        .mapValues(_.foldLeft(Bill.empty) (_ + _)) // using foldLeft to support collections of one.

      logger.debug("Obtaining the caller with the highest total call duration...")
      val (callerWithGreatestDuration, callerBill) = callerToBill
        .maxBy { case (_, bill) => bill.duration }
      logger.debug(s"${callerWithGreatestDuration.phoneNumber} will not be charged $callerBill.")


      // Removing the caller with the highest total call duration so that he is not charged.
      logger.info("Creating the final bill...")
      (callerToBill - callerWithGreatestDuration)
        .values
        .foldLeft(Bill.empty)(_ + _)
    }
}
