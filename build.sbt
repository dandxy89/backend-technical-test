val http4sVersion  = "0.18.17"
val circeVersion   = "0.9.3"
val scalatest      = "3.0.5"
val jodaTime       = "2.10"
val logbackVersion = "1.2.3"
val scalaLogging   = "3.5.0"

ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / name := "technical-test"
ThisBuild / version := Version()

def itFilter(name: String): Boolean = name endsWith "ITest"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(JavaAppPackaging)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "org.http4s"                 %% "http4s-dsl"          % http4sVersion,
      "org.http4s"                 %% "http4s-blaze-server" % http4sVersion,
      "org.http4s"                 %% "http4s-blaze-client" % http4sVersion,
      "org.http4s"                 %% "http4s-circe"        % http4sVersion,
      "io.circe"                   %% "circe-generic"       % circeVersion,
      "io.circe"                   %% "circe-parser"        % circeVersion,
      "joda-time"                  % "joda-time"            % jodaTime,
      "ch.qos.logback"             % "logback-classic"      % logbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging"       % scalaLogging,
      "org.scalatest"              %% "scalatest"           % scalatest % "it, test"
    ),
    ScalacOptions.settings
  )
