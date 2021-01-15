/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.model

import java.time.{Duration, LocalDateTime}

/**
  * Record of a call.
  *
  * @param start  When the call started (without timezone).
  * @param end    When the call ended (without timezone). It must be at the same time or occur after 'start'.
  * @param from   Who started the call.
  * @param to     To whom `from` is calling.
  */
sealed case class CallRecord(start: LocalDateTime, end: LocalDateTime, from: Contact, to: Contact) {
  require(end.compareTo(start) >= 0, s"'end' ($end) must be at the same time or occur after 'start' ($start)")

  /**
    * Non-negative duration of the call.
    */
  val duration: Duration = Duration.between(start, end)
}
