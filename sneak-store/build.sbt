name := "sneak-store"

version := "0.1"

scalaVersion := "2.10.1"

resolvers += "Twitter's Repository" at "http://maven.twttr.com/"


libraryDependencies ++= Seq(
  "com.twitter" % "cassie-core" % "0.25.3"
)