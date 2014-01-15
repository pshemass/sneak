import sbt.ExclusionRule
import sbt.Keys._

name := "sneak-commons"

organization := "sneak"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.3"

com.twitter.scrooge.ScroogeSBT.newSettings

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "scrooge-core" % "3.12.1",
  "com.twitter" %% "finagle-thrift" % "6.5.0",
  "org.apache" %% "kafka" % "0.8.0-SNAPSHOT" excludeAll(
    ExclusionRule(organization = "com.sun.jdmk"),
    ExclusionRule(organization = "com.sun.jmx"),
    ExclusionRule(organization = "javax.jms"))
)
