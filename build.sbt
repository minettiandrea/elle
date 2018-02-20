name := """elle"""
organization := "ch.wavein"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += guice
libraryDependencies += "ch.wavein" %% "scala-mailchimp" % "0.4.3"
libraryDependencies += "ch.wavein" %% "tdl-sdk" % "0.2.0-SNAPSHOT"



