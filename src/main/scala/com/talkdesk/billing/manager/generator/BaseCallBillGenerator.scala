/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager.generator

import java.time.Duration

import com.talkdesk.billing.manager.Types.Cost
import com.talkdesk.billing.model.CallRecord
import com.talkdesk.billing.manager.{Bill, CallBillGenerator}

import scala.collection.immutable.TreeMap
import scala.util.Try

/**
  * Companion object of [[com.talkdesk.billing.manager.generator.BaseCallBillGenerator]].
  */
object BaseCallBillGenerator {

  /**
    * Pricing plan:
    * <ul>
    *   <li>`]0, 5min]`: 5 cents per minute.</li>
    *   <li>`[5min, +oo]`: 2 cents per minute.</li>
    * </ul>
    */
  val PriceRange: TreeMap[Duration, Cost] = TreeMap(
    Duration.ofMinutes(5).minusSeconds(1)  -> 0.05,
    Duration.ofMinutes(5)                  -> 0.02
  )
}

/**
  * Generates a [[com.talkdesk.billing.manager.Bill]] given a [[CallRecord]].
  */
final class BaseCallBillGenerator extends CallBillGenerator {

  /**
    * The price per minute given the current duration of the call.
    *
    * @param duration The current duration.
    * @return The cost per minute at `duration`.
    */
  private def priceAt(duration: Duration): Cost = BaseCallBillGenerator.PriceRange(
    BaseCallBillGenerator.PriceRange.from(duration).headOption.map(_._1)
      .getOrElse(BaseCallBillGenerator.PriceRange.to(duration).lastKey)
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
