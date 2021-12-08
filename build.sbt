name := "cross-chain-wallet"

version := "0.1"

scalaVersion := "2.13.7"

idePackagePrefix := Some("com.ciphertrace")

Global / onChangedBuildSource := ReloadOnSourceChanges

resolvers += Resolver.sonatypeRepo("snapshots")

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

PB.targets in Compile := Seq(
  scalapb.gen(grpc = true) -> (sourceManaged in Compile).value / "scalapb",
  scalapb.zio_grpc.ZioCodeGenerator -> (sourceManaged in Compile).value / "scalapb"
)

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % "1.41.0",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion
)

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-bits" % "1.1.12",
  "org.bouncycastle" % "bcprov-jdk15on" % "1.68",
  "fr.acinq" %% "bitcoin-lib" % "0.20-SNAPSHOT"
)

