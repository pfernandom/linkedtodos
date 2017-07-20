name := """front"""
organization := "com.example"

version := "1.0-SNAPSHOT"

val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

externalResolvers := Seq(ivyLocal, Resolver.mavenLocal)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice

libraryDependencies += "com.linkedin.parseq" % "parseq" % "2.6.17"

libraryDependencies += "com.example" % "data-template" % "1.0"

libraryDependencies += "com.example" % "rest-client" % "1.0"

libraryDependencies += "com.example" % "client" % "1.0"