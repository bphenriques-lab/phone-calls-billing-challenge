/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.biller

import com.talkdesk.billing.model.Bill

/**
  * Creates a bill.
  *
  * @tparam T The billable type.
  */
trait Biller[T] {

  /**
    * Creates a bill.
    *
    * @param value The instance of `T`
    * @return The bill.
    */
  def createBill(value: T): Bill
}
