name := """CoachCentralDemo"""

version := "1.0.17"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  // If you enable PlayEbean plugin you must remove these
  // JPA dependencies to avoid conflicts.
  //javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.2.4.Final",
  "dom4j" % "dom4j" % "1.6.1" intransitive(),
  "be.objectify" %% "deadbolt-java" % "2.5.3",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
libraryDependencies += evolutions
libraryDependencies += jdbc

javaOptions ++= Seq(
	"-Dhttp.port=disabled",
   "-Dhttps.port=9443",
   "-Dhttps.keyStore=conf/coachcentraldev_orreco_com.jks",
   "-Dhttps.keyStorePassword=boxfuse")



//PlayKeys.externalizeResources := false


fork in run := false

import com.typesafe.sbt.packager.MappingsHelper._
    mappings in Universal ++= directory(baseDirectory.value / "data")
