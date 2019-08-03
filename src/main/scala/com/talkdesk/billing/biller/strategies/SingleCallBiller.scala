/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.biller.strategies

import java.time.Duration

import com.talkdesk.billing.biller.Biller
import com.talkdesk.billing.model.{Bill, CallRecord}

import scala.collection.immutable.TreeMap

/**
  * Companion object of [[SingleCallBiller]].
  */
object SingleCallBiller {

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
final class SingleCallBiller extends Biller[CallRecord] {

  /**
    * The price per minute given the current duration of the call.
    *
    * @param duration The current duration.
    * @return The cost per minute at `duration`.
    */
  private def priceAt(duration: Duration): BigDecimal = SingleCallBiller.PriceRange(
    SingleCallBiller.PriceRange.from(duration).headOption.map(_._1)
      .getOrElse(SingleCallBiller.PriceRange.to(duration).lastKey)
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
