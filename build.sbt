name := "basic-project"

organization := "example"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.2"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

initialCommands := "import example._"

libraryDependencies ++= Seq(
  "org.clapper" %% "grizzled-scala" % "1.3",
  "com.quantifind" %% "wisp" % "0.0.4"
)
