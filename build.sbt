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
  ),
  play27 = Seq(
    "com.typesafe.play" %% "play" % "2.7.4"
  )
)

val testDependencies = PlayCrossCompilation.dependencies(
  shared = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  ),
  play25 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.1" % Test
  ),
  play26 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  ),
  play27 = Seq(
    "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
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
    /*
     * set fork in Test true to avoid error when PLAY_VERSION=2.7:
     * - java.lang.AbstractMethodError: play.api.i18n.Messages$MessagesParser.scala$util$parsing$combinator$Parsers$$lastNoSuccessVar()Lscala/util/DynamicVariable;
     * see thread at: https://github.com/scala/scala-parser-combinators/issues/197
     */
    fork in Test := true
  )
  .settings(PlayCrossCompilation.playCrossCompilationSettings)
