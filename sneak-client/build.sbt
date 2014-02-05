name := "sneak-client"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies += "joda-time" % "joda-time" % "2.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"

libraryDependencies += "sneak" %% "sneak-commons" % "0.0.1-SNAPSHOT"

libraryDependencies += "org.specs2" %% "specs2" % "2.1.1" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"

scalacOptions in Test ++= Seq("-Yrangepos")
