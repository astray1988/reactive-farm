name := """twitter-stream"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

resolvers += "mandubian maven bintray" at "http://dl.bintray.com/mandubian/maven"


libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play.extras" % "iteratees-extras_2.11" % "1.5.0"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
