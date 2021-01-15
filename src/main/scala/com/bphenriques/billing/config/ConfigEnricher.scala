/*
 * © Copyright 2019 Bruno Henriques
 */

package com.bphenriques.billing.config

import com.typesafe.config.Config

import scala.util.Try

/**
  * Extension of [[com.typesafe.config.Config]].
  */
object ConfigEnricher {
  implicit class RichConfig(config: Config) {

    /**
      * Given a [[com.typesafe.config.Config]] create an instance of [[T]].
      *
      * @param parse The parser.
      * @tparam T    The target type.
      * @return Instance of [[T]]
      */
    def read[T](implicit parse: Config => Try[T]): Try[T] = parse(config)

    /**
      * Gets the first character of the String at the path specified.
      *
      * @param path The path.
      * @return The first character of the String.
      */
    def getChar(path: String): Char = config.getString(path).head
  }
}
