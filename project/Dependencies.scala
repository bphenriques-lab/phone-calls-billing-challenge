/*
 * © Copyright 2019 Bruno Henriques
 */

import sbt._

/**
  * Set of dependencies.
  */
object Dependencies {
  lazy val scalaTest            = "org.scalatest"              %% "scalatest"          % "3.0.5"
  lazy val kantanCSVGeneric     = "com.nrinaudo"               %% "kantan.csv-generic" % "0.5.0"
  lazy val typeSafeConfig       = "com.typesafe"                % "config"             % "1.3.2"
  lazy val logbackClassic       = "ch.qos.logback"              % "logback-classic"    % "1.2.3"
  lazy val scalaLogging         = "com.typesafe.scala-logging" %% "scala-logging"      % "3.9.2"
}