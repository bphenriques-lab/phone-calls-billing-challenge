/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.supplier

import java.io.File

import com.talkdesk.billing.model.CallRecord

import scala.util.Try

/**
  * Supplies a sequence of [[com.talkdesk.billing.model.CallRecord]] from a file.
  *
  * @author Bruno Henriques (brunoaphenriques@gmail.com)
  * @version 1.0.0-SNAPSHOT
  */
trait CallRecordsFileSupplier {

  /**
    * Returns a sequence of [[com.talkdesk.billing.model.CallRecord]] from a file.
    *
    * @param file The file.
    * @return The stream of [[com.talkdesk.billing.model.CallRecord]].
    */
  def from(file: File): Try[Seq[CallRecord]]
}
