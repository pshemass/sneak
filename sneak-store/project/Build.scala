import sbt._
import sbt.ExclusionRule
import sbt.ExclusionRule
import sbt.Keys._
import scala._

object SneakStoreBuild extends Build {

  val akkaVersion = "2.3.0-RC1"


  lazy val publishM2Configuration =
    TaskKey[PublishConfiguration]("publish-m2-configuration",
      "Configuration for publishing to the .m2 repository.")

  lazy val publishM2 =
    TaskKey[Unit]("publish-m2",
      "Publishes artifacts to the .m2 repository.")

  lazy val m2Repo =
    Resolver.file("publish-m2-local",
      Path.userHome / ".m2" / "repository")

  val projectSettings = Seq(

    version := "0.1",
    name := "sneak-store",

    scalaVersion := "2.10.3",

    scalacOptions in Test ++= Seq("-Yrangepos"),

    resolvers ++= Seq(
      "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
      "sonatype-public" at "https://oss.sonatype.org/content/groups/public"
    ),

    publishM2Configuration <<= (packagedArtifacts, checksums in publish, ivyLoggingLevel) map {
      (arts, cs, level) =>
        Classpaths.publishConfig(arts, None, resolverName = m2Repo.name, checksums = cs, logging = level)
    },
    publishM2 <<= Classpaths.publishTask(publishM2Configuration, deliverLocal),
    otherResolvers += m2Repo,


    libraryDependencies ++= Seq(
      "sneak" %% "sneak-commons" % "0.0.1-SNAPSHOT",
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
      "io.spray" % "spray-can" % "1.2-20130712",
      "io.spray" % "spray-client" % "1.2-20130712",
      "io.spray" % "spray-routing" % "1.2-20130712" exclude("com.chuusai", "shapeless_2.10"),
      "io.spray" %% "spray-json" % "1.2.3",
      "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.0-rc2",
      "org.xerial.snappy" % "snappy-java" % "1.1.0.1",
      "joda-time" % "joda-time" % "2.1",
      "org.joda" % "joda-convert" % "1.2",
      "org.apache" %% "kafka" % "0.8.0-SNAPSHOT" excludeAll(
        ExclusionRule(organization = "com.sun.jdmk"),
        ExclusionRule(organization = "com.sun.jmx"),
        ExclusionRule(organization = "javax.jms")
        ),
      "storm" % "storm" % "0.9.0.1",
//      "net.wurstmeister.storm" % "storm-kafka-0.8-plus" % "0.3.0-SNAPSHOT",
      "com.github.velvia" %% "scala-storm" % "0.2.3-SNAPSHOT",
      "com.typesafe" %% "scalalogging-slf4j" % "1.0.1",
      "ch.qos.logback" % "logback-classic" % "1.0.13",
      "org.specs2" %% "specs2" % "2.3.7" % "test",
      "org.scalatest" %% "scalatest" % "2.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.11.3" % "test",
      "org.mockito" % "mockito-all" % "1.9.5")

  )

  lazy val root = Project(
    id = "sneak-store",
    base = file("."),
    settings = Project.defaultSettings ++ projectSettings
  ).settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)


}