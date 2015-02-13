name := "Narscala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.9",
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2"
)

mainClass in Compile := Some("com.narscala.Main")