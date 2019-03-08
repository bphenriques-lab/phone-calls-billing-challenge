/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager

import java.time.Duration

import com.talkdesk.billing.manager.Types.{Cost, CostFormat}

/**
  * Represents the bill of one or several phone calls.
  *
  * @param duration The total duration of the billable calls.
  * @param cost     The total cost of this bill.
  */
sealed case class Bill(duration: Duration, cost: Cost) {
  require(!duration.isNegative, "A bill has always a non-negative duration.")
  require(cost >= 0, "A bill has always a non-negative cost.")

  /**
    * Creates a new [[com.talkdesk.billing.manager.Bill]] given another [[com.talkdesk.billing.manager.Bill]].
    *
    * @param other The other bill.
    * @return The current bill with the new record.
    */
  def +(other: Bill): Bill = Bill(
    duration.plus(other.duration),
    cost + other.cost
  )

  /**
    * Returns the String representation of the Bill using the provided [[Types.CostFormat]].
    *
    * @param format The format.
    * @return This bill formatted.
    */
  def format(format: CostFormat): String = format.format(cost)

  /**
    * @inheritdoc
    */
  override def toString: String = format(Bill.DefaultFormat)
}

/**
  * Companion object of [[com.talkdesk.billing.manager.Bill]].
  */
object Bill {

  /**
    * Default representation of [[Types.Cost]].
    */
  val DefaultFormat: CostFormat = new CostFormat("0.00")

  /**
    * Creates an empty [[com.talkdesk.billing.manager.Bill]].
    *
    * @return Empty bill.
    */
  def empty: Bill = Bill(Duration.ZERO, 0)
}
