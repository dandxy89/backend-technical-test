name := "technical-test"

scalaVersion := "2.12.6"

version := Version()

val http4sVersion = "0.18.17"
val circeVersion  = "0.9.3"
val scalatest     = "3.0.5"
val jodaTime      = "2.10"

libraryDependencies ++= Seq(
  "org.http4s"    %% "http4s-dsl"          % http4sVersion,
  "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"    %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"    %% "http4s-circe"        % http4sVersion,
  "io.circe"      %% "circe-generic"       % circeVersion,
  "io.circe"      %% "circe-parser"        % circeVersion,
  "joda-time"     % "joda-time"            % jodaTime,
  "org.scalatest" %% "scalatest"           % scalatest % "test",
)

ScalacOptions.settings

parallelExecution in Test := false

fork in Test := false
