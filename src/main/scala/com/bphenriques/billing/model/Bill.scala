/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

import java.text.DecimalFormat
import java.time.Duration

/**
  * Represents the bill of one or several phone calls.
  *
  * @param duration The total duration of the billable calls.
  * @param cost     The total cost of this bill.
  */
sealed case class Bill(duration: Duration, cost: BigDecimal) {
  require(!duration.isNegative, "A bill has always a non-negative duration.")
  require(cost >= 0, "A bill has always a non-negative cost.")

  /**
    * Creates a new [[Bill]] given another [[Bill]].
    *
    * @param other The other bill.
    * @return The current bill with the new record.
    */
  def +(other: Bill): Bill = Bill(
    duration.plus(other.duration),
    cost + other.cost
  )

  /**
    * Returns the String representation of the Bill.
    *
    * @param format The format.
    * @return This bill formatted.
    */
  def format(format: DecimalFormat): String = format.format(cost)

  /**
    * @inheritdoc
    */
  override def toString: String = format(Bill.DefaultFormat)
}

/**
  * Companion object of [[Bill]].
  */
object Bill {

  /**
    * Default formater.
    */
  val DefaultFormat: DecimalFormat = new DecimalFormat("0.00")

  /**
    * Creates an empty [[Bill]].
    *
    * @return Empty bill.
    */
  def empty: Bill = Bill(Duration.ZERO, 0)
}
