import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName         = "passtest"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.ryantenney.passkit4j" % "passkit4j" % "1.0.0",
    "org.webjars" % "webjars-play" % "2.0",
    "org.webjars" % "bootstrap" % "2.1.1"


  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    // Add your own project settings here      
  )

}

