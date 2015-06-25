name := "akka-http-macwire"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-Ydead-code",
  "-Ywarn-numeric-widen",
  "-Xfatal-warnings",
  "-encoding", "UTF-8"
)

resolvers ++= Seq(
  "Typesafe Repo"     at "http://repo.typesafe.com/typesafe/repo/",
  "Sonatype OSS Repo" at "https://oss.sonatype.org/content/repositories/releases",
  "Scalaz Bintray"    at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++= {
  val akkaV = "2.3.11"
  val akkaHttpV = "1.0-RC4"
  val macwireV = "1.0.1"
  val specs2V = "3.4"
  Seq(
    // ----- HTTP -----
    "com.typesafe.akka" %% "akka-http-experimental"      % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaHttpV,
    // ----- DI -----
    "com.softwaremill.macwire" %% "macros"  % macwireV,
    "com.softwaremill.macwire" %% "runtime" % macwireV,
    // ----- Logging -----
    "com.typesafe.akka" %% "akka-slf4j"      % akkaV,
    "ch.qos.logback"    %  "logback-classic" % "1.1.3",
    // ----- Test -----
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaHttpV % "test",
    "org.specs2"        %% "specs2-core"                    % specs2V   % "test",
    "org.specs2"        %% "specs2-mock"                    % specs2V   % "test",
    "org.specs2"        %% "specs2-scalacheck"              % specs2V   % "test"
  )
}

Revolver.settings

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value

(test in Test) <<= (test in Test) dependsOn compileScalastyle

wartremoverErrors in (Compile, compile) ++= Warts.allBut(
  Wart.DefaultArguments,
  Wart.NoNeedForMonad,
  Wart.NonUnitStatements
)
