name := "Narscala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
    "Rediscala at Bintray" at "http://dl.bintray.com/etaty/maven"
)

scalacOptions ++= Seq(
    "-deprecation",
    "-feature"
)

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.2.1",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    "org.parboiled" %% "parboiled" % "2.0.1",
    "com.etaty.rediscala" %% "rediscala" % "1.4.0",
    "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

// Test Options
testOptions := Seq(Tests.Filter(s => Seq("Spec", "Unit").exists(s.endsWith(_))))
logBuffered := false