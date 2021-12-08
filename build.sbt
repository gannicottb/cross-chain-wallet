name := "cross-chain-wallet"

version := "0.1"

scalaVersion := "2.13.7"

idePackagePrefix := Some("com.ciphertrace")

Global / onChangedBuildSource := ReloadOnSourceChanges

val zioVersion = "1.0.12"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "io.d11" %% "zhttp" % "1.0.0.0-RC17"
)

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
