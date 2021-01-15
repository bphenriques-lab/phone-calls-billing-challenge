/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.exception

/**
  * Generic Billing exception.
  *
  * @param message    The message.
  * @param error      The error.
  */
case class BillingException(
  message: String,
  error: Throwable
) extends Exception(message, error)
