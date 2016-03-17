import sbt._
import sbt.Keys._
import scoverage.ScoverageKeys
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import uk.gov.hmrc.DefaultBuildSettings._

  val appName = "play-whitelist-filter"

  lazy val PlayWhitelistFilter = Project(appName, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      ScoverageKeys.coverageExcludedPackages := "uk.gov.hmrc.BuildInfo",
      targetJvm := "jvm-1.8",
      libraryDependencies ++= Seq(
        "com.typesafe.play" %% "play" % "2.4.6",
        "org.scalactic" %% "scalactic" % "2.2.6" % "test",
        "org.scalatest" %% "scalatest" % "2.2.6" % "test",
        "org.scalatestplus" %% "play" % "1.4.0" % "test",
        "org.pegdown" % "pegdown" % "1.5.0" % "test"
      ),
      Developers()
    )
}

object Developers {
  def apply() = developers := List[Developer]()
}

