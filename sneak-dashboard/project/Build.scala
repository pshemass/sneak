import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sneak-dashboard"
  val appVersion      = "1.0-SNAPSHOT"


  val appDependencies = Seq(
    "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.0.4" % "test"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
