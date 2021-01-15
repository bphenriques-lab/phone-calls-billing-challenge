/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing

import com.bphenriques.billing.config.AppConfig

import java.io.File
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success, Try}

/**
  * Main application that receives the path to a file as its single argument.
  * <p>
  * Exits with status code `0` if successful or `-1` otherwise.
  */
object Main extends App with Application with LazyLogging {

  if (args.length != 1) {
    println("Usage: <file>")
    System.exit(-1)
  }

  // Process arguments.
  val filename = args(0)

  val statusCode = process(filename) match {
    case Success(_) => 0
    case Failure(exception) =>
      logger.error(s"Error reading file $filename.", exception)
      -1
  }

  sys.exit(statusCode)
}

/**
  * Main application.
  */
trait Application {

  /**
    * Prints to `stdout` the cost of the calls in the file.
    *
    * @param filename The file to process.
    */
  def process(filename: String): Try[Unit] = Try {
    import com.bphenriques.billing.config.ConfigEnricher._

    (for {
      config <- ConfigFactory.load().read[AppConfig](AppConfig.fromConfig).toEither
      records <- config.callRecordsSupplier.from(new File(filename)).toEither
    } yield {
      val bill = config.biller.createBill(records)
      println(bill.format(config.costFormat))
    }) match {
      case Left(error) => throw error
      case _           =>
    }
  }
}
