name := """front"""
organization := "com.example"

version := "1.0-SNAPSHOT"

resolvers += "Maven Central Server" at "http://repo1.maven.org/maven2"

resolvers += Resolver.mavenLocal

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += "com.linkedin.parseq" % "parseq" % "2.6.17"

libraryDependencies += "com.example" % "data-template" % "1.0"

libraryDependencies += "com.example" % "rest-client" % "1.0"

libraryDependencies += "com.example" % "client" % "1.0"