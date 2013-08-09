name := "sneak-store"

version := "0.1"

scalaVersion := "2.10.1"


libraryDependencies +=  "com.twitter" % "cassie-core" % "0.25.3"

libraryDependencies += "joda-time" % "joda-time" % "2.1"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "org.apache" %% "kafka" % "0.8.0-SNAPSHOT"

libraryDependencies += "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"

libraryDependencies += "sneak" %% "sneak-commons" % "0.0.1"


scalacOptions in Test ++= Seq("-Yrangepos")
