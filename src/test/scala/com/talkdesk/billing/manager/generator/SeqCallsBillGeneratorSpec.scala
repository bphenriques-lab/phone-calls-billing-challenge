/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.manager.generator

import java.io.File

import com.talkdesk.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor2

import scala.io.Source

/**
  * Tests [[com.talkdesk.billing.manager.generator.BaseSeqCallsBillGenerator]].
  */
class SeqCallsBillGeneratorSpec extends BaseSpec {

  /**
    * Target instance to test.
    */
  private val callRecordsManager = new BaseSeqCallsBillGenerator(new BaseCallBillGenerator)

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
