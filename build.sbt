name := "Narscala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.2.1",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    "org.parboiled" %% "parboiled" % "2.0.1"
)