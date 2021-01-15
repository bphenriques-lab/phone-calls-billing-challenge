/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.biller.strategies

import com.bphenriques.billing.model.{CallRecord, Contact}

import java.time.Duration
import com.bphenriques.billing.model.CallRecord
import com.bphenriques.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

/**
  * Tests [[CallRecordBillFactory]].
  */
class CallRecordBillFactorySpec extends BaseSpec {

  /**
    * Target instance to test.
    */
  private val callPricing = new CallRecordBillFactory()

  /**
    * Price per minute during the first 5 minutes of the call (4:59).
    */
  private val PRICE_PER_MINUTE_FIRST_5_MIN = CallRecordBillFactory.PriceRange(Duration.ofMinutes(5).minusSeconds(1))

  /**
    * Price per minute after 5 minutes (5:00)
    */
  private val PRICE_PER_MINUTE_AFTER_5_MIN = CallRecordBillFactory.PriceRange(Duration.ofMinutes(5))

  /**
    * Creates a sample [[CallRecord]]. The call will start at noon and will be from "A" to "B".
    *
    * @param duration The duration of the call.
    * @return The [[CallRecord]].
    */
  def callRecord(duration: Duration) = CallRecord(
    Noon,
    Noon.plus(duration),
    Contact("A"),
    Contact("B")
  )

  it must "return the correct cost" in {
    val calls = Table(
      ("Call", "Cost"),

      // Edge cases: Zero and start com.bphenriques.billing the call.
      (callRecord(Duration.ofSeconds(0)),  BigDecimal(0)),
      (callRecord(Duration.ofSeconds(1)),  1 * PRICE_PER_MINUTE_FIRST_5_MIN),

      // Edge cases: Before and after the first minute.
      (callRecord(Duration.ofSeconds(59)), 1 * PRICE_PER_MINUTE_FIRST_5_MIN),
      (callRecord(Duration.ofSeconds(60)), 2 * PRICE_PER_MINUTE_FIRST_5_MIN),

      // Edge cases: The cost before and after starting to charge differently after 5 minutes.
      (callRecord(Duration.ofMinutes(5).minus(Duration.ofSeconds(1))), 5 * PRICE_PER_MINUTE_FIRST_5_MIN),
      (callRecord(Duration.ofMinutes(5)), 5 * PRICE_PER_MINUTE_FIRST_5_MIN + 1 * PRICE_PER_MINUTE_AFTER_5_MIN),

      // Average case: The cost of a 10 minutes call.
      (callRecord(Duration.ofMinutes(10)), 5 * PRICE_PER_MINUTE_FIRST_5_MIN + 6 * PRICE_PER_MINUTE_AFTER_5_MIN),
    )

    forAll(calls) { (callRecord: CallRecord, expectedCost: BigDecimal) =>
      callPricing.createBill(callRecord).cost shouldEqual expectedCost
    }
  }
}
