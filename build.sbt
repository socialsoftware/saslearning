organization := "pt.ulisboa.tecnico"
name := "saslearning"
version := "2.0-SNAPSHOT"

scalaVersion := "2.12.2"

val finchVersion = "0.14.0"
val circeVersion = "0.8.0"
val googleOAuthVersion = "1.22.0"

libraryDependencies ++= Seq(
  "com.github.finagle" %% "finch-core" % finchVersion,
  "com.github.finagle" %% "finch-circe" % finchVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "com.twitter" %% "twitter-server" % "1.29.0",
  //OAuth
  "com.google.oauth-client" % "google-oauth-client" % googleOAuthVersion,
  "com.google.oauth-client" % "google-oauth-client-jetty" % googleOAuthVersion,
  "com.google.http-client" % "google-http-client-jackson2" % googleOAuthVersion
)