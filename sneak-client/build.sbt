name := "sneak-client"

version := "1.0"

scalaVersion := "2.10.1"

libraryDependencies += "joda-time" % "joda-time" % "2.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "org.apache" %% "kafka" % "0.8.0-SNAPSHOT"

libraryDependencies += "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"

libraryDependencies += "sneak" %% "sneak-commons" % "0.0.1"

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
