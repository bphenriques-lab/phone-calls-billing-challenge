/*
 * © Copyright 2019 Bruno Henriques
 */

package com.talkdesk.billing.supplier

import java.io.File
import java.nio.file.Files

import com.talkdesk._
import com.talkdesk.billing.model.{CallRecord, Contact}
import com.talkdesk.helpers.BaseSpec
import org.scalatest.prop.TableDrivenPropertyChecks._

import scala.util.Success

/**
  * Tests [[billing.supplier.csv.CSVCallRecordsFileSupplier]].
  */
class CsvCallRecordsFileSupplierSpec extends BaseSpec {

  it must "reject folders" in {
    val files = Table("Folder", ValidSamplesFolder, InvalidSamplesFolder)

    forAll (files) { file: File =>
      DefaultRecordsSupplier.from(file).isFailure shouldBe true
    }
  }

  it must "reject badly formatted files" in {
    val files = Table("File", InvalidSamplesFolder.listFiles:_*)

    forAll (files) { file: File =>
      DefaultRecordsSupplier.from(file).isFailure shouldBe true
    }
  }

  it must "accept valid files" in {
    val files = Table("File", ValidSamplesFolder.listFiles.filter(_.getName.endsWith(".csv")): _*)

    forAll (files) { file: File =>
      DefaultRecordsSupplier.from(file).isSuccess shouldBe true
    }
  }

  it must "parse correctly calls that ended after midnight" in {
    val readRecords = DefaultRecordsSupplier.from(getValidResource("after-midnight.csv"))
    val expectedRecords = Stream(
      CallRecord(ReferenceDate.atTime(23, 58, 0), ReferenceDate.plusDays(1).atTime(0, 1, 59), Contact("A"), Contact("B")),
      CallRecord(ReferenceDate.atTime(23, 50, 0), ReferenceDate.plusDays(1).atTime(0, 10, 0), Contact("B"), Contact("A")),
    )

    readRecords shouldEqual Success(expectedRecords)
  }

  it must "parse correctly the records" in {
    val readRecords = DefaultRecordsSupplier.from(getValidResource("sample.csv"))
    val expectedRecords = Stream(
      CallRecord(localDataTimeAt(9, 11, 30), localDataTimeAt(9, 15, 22), Contact("+351914374373"), Contact("+351215355312")),
      CallRecord(localDataTimeAt(15, 20, 4), localDataTimeAt(15, 23,49), Contact("+351217538222"), Contact("+351214434422")),
      CallRecord(localDataTimeAt(16, 43, 2), localDataTimeAt(16, 50, 20), Contact("+351217235554"), Contact("+351329932233")),
      CallRecord(localDataTimeAt(17, 44, 4), localDataTimeAt(17, 49, 30), Contact("+351914374373"), Contact("+351963433432")),
    )

    readRecords shouldEqual Success(expectedRecords)
  }

  it must "when not validating the file, it should return all valid lines" in {
    val readRecords = SkipInvalidLinesRecordsSupplier.from(getInvalidResource("invalid-to-contact.csv"))

    val expectedRecords = Stream(
      CallRecord(localDataTimeAt(9, 11, 30), localDataTimeAt(9, 15, 22), Contact("+351914374373"), Contact("+351215355312")),
      CallRecord(localDataTimeAt(15, 20, 4), localDataTimeAt(15, 23,49), Contact("+351217538222"), Contact("+351214434422")),
      CallRecord(localDataTimeAt(17, 44, 4), localDataTimeAt(17, 49, 30), Contact("+351914374373"), Contact("+351963433432")),
    )

    readRecords shouldEqual Success(expectedRecords)
  }
}