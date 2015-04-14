name := "akka-http-macwire"

version := "1.0"

scalaVersion := "2.11.6"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Xfatal-warnings",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(
  "Typesafe Repo"     at "http://repo.typesafe.com/typesafe/repo/",
  "Sonatype OSS Repo" at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val akkaHttpV = "1.0-M5"
  val macwireV = "1.0.1"
  Seq(
    // ----- HTTP -----
    "com.typesafe.akka" %% "akka-http-experimental"      % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaHttpV,
    // ----- DI -----
    "com.softwaremill.macwire" %% "macros"  % macwireV,
    "com.softwaremill.macwire" %% "runtime" % macwireV,
    // ----- Logging -----
    "com.typesafe.akka" %% "akka-slf4j"      % akkaV,
    "ch.qos.logback"    %  "logback-classic" % "1.0.13"
  )
}

Revolver.settings
