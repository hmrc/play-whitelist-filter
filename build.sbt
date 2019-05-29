import sbt.Keys._
import sbt._
import uk.gov.hmrc.versioning.SbtGitVersioning

val appName = "play-whitelist-filter"

val compileDependencies = PlayCrossCompilation.dependencies(
  play25 = Seq(
    "com.typesafe.play" %% "play" % "2.5.19"
  ),
  play26 = Seq(
    "com.typesafe.play" %% "play" % "2.6.20"
  )
)

val testDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  ),
  play25 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % "test"
  ),
  play26 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
  )
)

lazy val playWhitelistFilter = (project in file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory)
  .settings(
    name := appName,
    majorVersion := 3,
    makePublicallyAvailableOnBintray := true,
    libraryDependencies ++= compileDependencies ++ testDependencies,
    scalaVersion := "2.11.12",
    crossScalaVersions := Seq("2.11.12", "2.12.8"),
    developers := List[Developer]()
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
