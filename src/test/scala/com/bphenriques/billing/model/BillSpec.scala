/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

import java.text.DecimalFormat
import java.time.Duration

import com.bphenriques.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

/**
  * Tests [[Bill]].
  */
class BillSpec extends BaseSpec {

  it must "validate its arguments" in {
    val rows = Table(
      ("Duration", "Cost"),
      (Duration.ZERO.minusNanos(1), BigDecimal(1)), // Negative duration.
      (Duration.ZERO,               BigDecimal(-1)) // Negative cost.
    )

    forAll (rows) { (duration: Duration, cost: BigDecimal) =>
      assertThrows[Exception] {
        Bill(duration, cost)
      }
    }
  }

  it must "respect the nature of a valid empty bill" in {
    val emptyBill = Bill.empty

    emptyBill.cost shouldEqual 0
    emptyBill.duration shouldEqual Duration.ZERO
  }

  it must "read correctly its parameters" in {
    val rows = Table(
      ("Duration", "Cost"),
      (Duration.ZERO,                 BigDecimal(1)), // Zero duration.
      (Duration.ZERO.plusSeconds(1),  BigDecimal(0)), // Zero cost.
      (Duration.ZERO.plusSeconds(1),  BigDecimal(2)), // Average case.
    )

    forAll (rows) { (duration: Duration, cost: BigDecimal) =>
      val bill = Bill(duration, cost)

      bill.duration shouldEqual duration
      bill.cost shouldEqual cost
    }
  }

  it must "sum correctly other bills" in {

    // empty bills.
    Bill.empty + Bill.empty shouldEqual Bill.empty

    val bill1    = Bill(Duration.ofMinutes(1), 1)
    val bill2    = Bill(Duration.ofMinutes(2), 2)
    val sumBills = Bill(Duration.ofMinutes(3), 3)

    // check commutative property.
    bill1 + bill2 shouldEqual sumBills
    bill2 + bill1 shouldEqual sumBills

    // check neutral bill.
    bill1      + Bill.empty shouldEqual bill1
    Bill.empty + bill1      shouldEqual bill1
  }

  it must "use the format provided as argument" in {
    val rows = Table(
      ("Bill", "Format", "Expected String"),
      (Bill(Duration.ofMinutes(1), 1),          Bill.DefaultFormat,          "1.00"),
      (Bill(Duration.ofMinutes(1), 1001024.32), Bill.DefaultFormat,          "1001024.32"),
      (Bill(Duration.ofMinutes(1), 1001024.32), new DecimalFormat("###,##0.00"), "1,001,024.32")
    )

    forAll (rows) { (bill: Bill, format: DecimalFormat, expected: String) =>
      bill.format(format) shouldEqual expected
    }
  }
}
