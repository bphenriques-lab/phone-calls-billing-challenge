/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

import com.bphenriques.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks.forAll
import org.scalatest.prop.TableDrivenPropertyChecks._

/**
  * Tests [[Contact]].
  */
class ContactSpec extends BaseSpec {

  it must "reject invalid contacts" in {
    val contact = Table("Contact", "", " ")

    forAll (contact) { contact: String =>
      assertThrows[Exception] {
        Contact(contact)
      }
    }
  }

  it must "accept valid contact" in {
    val contacts = Table(
      ("Contact",         "Expected PhoneNumber"),
      ("+351914374373",   "+351914374373"),
      ("00351914374373",  "00351914374373"),
      (" +351914374373 ", " +351914374373 "),
      ("631-960-7187",    "631-960-7187")
    )

    forAll (contacts) { (phoneNumber: String, expectedPhoneNumber: String) =>
      Contact(phoneNumber).phoneNumber shouldEqual expectedPhoneNumber
    }
  }
}
