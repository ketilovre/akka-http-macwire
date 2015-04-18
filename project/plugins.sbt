logLevel := Level.Info

resolvers ++= Seq(
  "Spray Repo" at "http://repo.spray.io"
)

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")

addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.12")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "0.94.6")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")
