/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

import java.time.{Duration, LocalDateTime, LocalTime}

import com.bphenriques.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

/**
  * Tests [[CallRecord]].
  */
class CallRecordSpec extends BaseSpec {

  it must "reject invalid call records" in {
    val records = Table(
      ("start",        "end",                   "from",    "to"),
      (LocalDateTime.MAX,  LocalDateTime.MIN,   "1234",    "1234"),   // start > end.
      (LocalDateTime.MIN,  LocalDateTime.MAX,   "",        "1234"),   // invalid from.
      (LocalDateTime.MIN,  LocalDateTime.MAX,   "1234",    " ")       // invalid to.
    )

    forAll(records) { (start: LocalDateTime, end: LocalDateTime, from: String, to: String) =>
      assertThrows[Exception] {
        CallRecord(start, end, Contact(from), Contact(to))
      }
    }
  }

  it must "read valid call records" in {
    val records = Table(
      ("start",             "end",               "from",    "to"),
      (LocalDateTime.MIN,   LocalDateTime.MIN,   "1234",    "1234"),  // edge case: start == end (lower bound).
      (LocalDateTime.MAX,   LocalDateTime.MAX,   "1234",    "1234"),  // edge case: start == end (higher bound).
      (LocalDateTime.MIN,   LocalDateTime.MAX,   "1234",    "1234"),  // edge case: from == to.
      (LocalDateTime.MIN,   LocalDateTime.MAX,   "1234",    "4321")   // average case.
    )

    forAll(records) { (start: LocalDateTime, end: LocalDateTime, from: String, to: String) =>
      val record = CallRecord(start, end, Contact(from), Contact(to))

      record.start              shouldEqual start
      record.end                shouldEqual end
      record.from.phoneNumber   shouldEqual from
      record.to.phoneNumber     shouldEqual to

      record.duration shouldEqual Duration.between(start, end)
    }
  }

  it must "return the expected-cost call duration" in {
    val records = Table(
      ("start",                         "end",                          "elapsedSeconds"),
      (LocalDateTime.MIN,               LocalDateTime.MIN,              0L),              // edge case: start == end (lower bound).
      (LocalDateTime.MAX,               LocalDateTime.MAX,              0L),              // edge case: start == end (higher bound).
      (localDataTimeAt(LocalTime.MIN),  localDataTimeAt(LocalTime.MAX), 24L*60*60 - 1),   // edge case: 00:00 -> 23:59.
      (Midnight,                        Noon,                           12L*60*60)        // average case: half day.
    )

    forAll(records) { (start: LocalDateTime, end: LocalDateTime, expectedDuration: Long) =>
      val record = CallRecord(start, end, Contact("from"), Contact("to"))

      record.duration.getSeconds shouldEqual expectedDuration
    }
  }
}
