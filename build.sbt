name := "Narscala"

version := "1.0"

scalaVersion := "2.11.5"

resolvers ++= Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
    "JBoss Repository" at "http://repository.jboss.org/nexus/content/groups/public/"
)

scalacOptions ++= Seq(
    "-deprecation",
    "-feature"
)

val droolsVersion = "6.2.0.CR4"

libraryDependencies ++= Seq(
	"com.typesafe" % "config" % "1.2.1",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.4-SNAPSHOT",
    "org.parboiled" %% "parboiled" % "2.0.1",
    "org.specs2" %% "specs2-core" % "2.4.15" % "test"
)

libraryDependencies ++= {
  "org.kie" % "kie-api" % droolsVersion ::
    List("drools-compiler", "drools-core", "drools-jsr94", "drools-decisiontables", "knowledge-api")
      .map("org.drools" % _ % droolsVersion)
}

// Test Options
testOptions := Seq(Tests.Filter(s => Seq("Spec", "Unit").exists(s.endsWith(_))))
logBuffered := false