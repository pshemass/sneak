import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sneak"
  val appVersion      = "1.0-SNAPSHOT"


  val twttr = "Twitter's Repository" at "http://maven.twttr.com/"
  val cassie = "com.twitter" % "cassie" % "0.19.0" excludeAll(
    ExclusionRule(organization = "javax.jms"),
    ExclusionRule(organization = "com.sun.jdmk"),
    ExclusionRule(organization = "com.sun.jmx")
  )

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    cassie
  )

  val agentProject = Project("agent-library", file("modules/agent-library"))

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += twttr
  ).dependsOn(agentProject)

}
