/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.biller.strategies

import java.io.File

import com.bphenriques.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor2

import scala.io.Source

/**
  * Tests [[SeqCallRecordBillFactory]].
  */
class SeqCallRecordBillFactorySpec extends BaseSpec {

  /**
    * Target instance to test.
    */
  private val callRecordsManager = new SeqCallRecordBillFactory(new CallRecordBillFactory)

  /**
    * The input files.
    */
  private val inputFiles = ValidSamplesFolder.listFiles.filter(_.getName.endsWith(".csv"))

  /**
    * The corresponding files with the expected cost.
    */
  private val expectedFiles = inputFiles.map(input => new File(input.getAbsolutePath + ".expected"))

  it must "return the correct cost" in {
    val testFiles: TableFor2[File, File] = Table(
      ("File",        "Expected"),
      inputFiles.zip(expectedFiles): _*
    )

    forAll(testFiles) { (input: File, expectedOutputFile: File) => {
      val sourceExpectedReturn = Source.fromFile(expectedOutputFile)
      val expectedReturn = sourceExpectedReturn.mkString
      sourceExpectedReturn.close()

      val bill = callRecordsManager.createBill(DefaultRecordsSupplier.from(input).get)
      bill.format(DefaultCostFormat) shouldEqual expectedReturn
    }}
  }
}
