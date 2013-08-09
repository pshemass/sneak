name := "sneak-commons"

organization := "sneak"

version := "0.0.1"

scalaVersion := "2.10.1"

com.twitter.scrooge.ScroogeSBT.newSettings

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "scrooge-runtime" % "3.1.5",
  "com.twitter" %% "finagle-thrift" % "6.4.0"
)