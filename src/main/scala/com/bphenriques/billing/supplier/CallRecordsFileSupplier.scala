/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.supplier

import com.bphenriques.billing.model.CallRecord

import java.io.File
import scala.util.Try

/**
  * Supplies a sequence of [[CallRecord]] from a file.
  *
  * @author Bruno Henriques (brunoaphenriques@gmail.com)
  * @version 1.0.0-SNAPSHOT
  */
trait CallRecordsFileSupplier {

  /**
    * Returns a sequence of [[CallRecord]] from a file.
    *
    * @param file The file.
    * @return The stream of [[CallRecord]].
    */
  def from(file: File): Try[Seq[CallRecord]]
}
