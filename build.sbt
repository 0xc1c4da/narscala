name := "Narscala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

scalacOptions ++= Seq(
    "-deprecation",
    "-feature"
)

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.2.1",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    "org.parboiled" %% "parboiled" % "2.0.1",
    "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

// Test Options
logBuffered := false