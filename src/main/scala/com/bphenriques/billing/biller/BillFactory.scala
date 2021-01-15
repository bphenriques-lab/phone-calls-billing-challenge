/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.biller

import com.bphenriques.billing.model.Bill

/**
  * Creates a bill.
  *
  * @tparam T The billable type.
  */
trait BillFactory[T] {

  /**
    * Creates a bill.
    *
    * @param value The instance of `T`
    * @return The bill.
    */
  def createBill(value: T): Bill
}
