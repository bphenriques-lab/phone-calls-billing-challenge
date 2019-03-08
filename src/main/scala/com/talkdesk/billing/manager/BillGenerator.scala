/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager

import com.talkdesk.billing.model.CallRecord

/**
  * Creates a bill.
  *
  * @tparam T The billable type.
  */
trait BillGenerator[T] {

  /**
    * Creates a bill.
    *
    * @param value The instance of `T`
    * @return The bill.
    */
  def createBill(value: T): Bill
}

/**
  * Creates a bill given a [[com.talkdesk.billing.model.CallRecord]].
  */
trait CallBillGenerator extends BillGenerator[CallRecord]

/**
  * Creates a bill given a sequence of [[com.talkdesk.billing.model.CallRecord]].
  */
trait SeqCallsBillGenerator extends BillGenerator[Seq[CallRecord]]
