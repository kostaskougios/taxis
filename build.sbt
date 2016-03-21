name := "taxis"

version := "1.0"

scalaVersion := "2.11.4"

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-target:jvm-1.7", "-unchecked", "-feature")

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.7"

libraryDependencies += "com.typesafe.akka" %% "akka-kernel" % "2.3.7"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.3.7"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.7" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.10.8" % "test"
