name := "kafka-streams-basics-scala"

organization := "com.github.piotr-kalanski"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.3"

resolvers += "confluent" at "http://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "org.apache.kafka" % "kafka-streams" % "0.11.0.0",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-log4j12" % "1.7.25"
)
