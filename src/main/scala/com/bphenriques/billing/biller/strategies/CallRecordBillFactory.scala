/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.biller.strategies

import com.bphenriques.billing.biller.BillFactory
import com.bphenriques.billing.model.{Bill, CallRecord}

import java.time.Duration
import com.bphenriques.billing.model.CallRecord

import scala.collection.immutable.TreeMap

/**
  * Companion object of [[CallRecordBillFactory]].
  */
object CallRecordBillFactory {

  /**
    * Pricing plan:
    * <ul>
    *   <li>`]0, 5min[`: 5 cents per minute.</li>
    *   <li>`[5min, +oo]`: 2 cents per minute.</li>
    * </ul>
    */
  val PriceRange: TreeMap[Duration, BigDecimal] = TreeMap(
    Duration.ofMinutes(5).minusSeconds(1)  -> 0.05,
    Duration.ofMinutes(5)                  -> 0.02
  )
}

/**
  * Generates a [[Bill]] given a [[CallRecord]].
  */
final class CallRecordBillFactory extends BillFactory[CallRecord] {

  /**
    * The price per minute given the current duration of the call.
    *
    * @param duration The current duration.
    * @return The cost per minute at `duration`.
    */
  private def priceAt(duration: Duration): BigDecimal = CallRecordBillFactory.PriceRange(
    CallRecordBillFactory.PriceRange.from(duration).headOption.map(_._1)
      .getOrElse(CallRecordBillFactory.PriceRange.to(duration).lastKey)
  )

  /**
    * @inheritdoc
    */
  override def createBill(report: CallRecord): Bill =
    if (report.duration.isZero) {
      Bill.empty
    } else {
      Bill(
        report.duration,
        (0L to report.duration.toMinutes)
          .map(m => Duration.ofMinutes(m))
          .map(priceAt)
          .sum
      )
    }
}
