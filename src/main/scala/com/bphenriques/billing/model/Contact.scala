/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

/**
  * Phone Contact.
  *
  * @param phoneNumber The non-empty phone number.
  */
sealed case class Contact(phoneNumber: String) {
  require(phoneNumber.trim().length > 0, "phoneNumber must be a non-empty String")
}
