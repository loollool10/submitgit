name := "submitgit"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

updateOptions := updateOptions.value.withCachedResolution(true)

lazy val root = (project in file(".")).enablePlugins(
  PlayScala,
  BuildInfoPlugin
).settings(
  buildInfoKeys := Seq[BuildInfoKey](
    name,
    BuildInfoKey.constant("gitCommitId", Option(System.getenv("BUILD_VCS_NUMBER")) getOrElse(try {
      "git rev-parse HEAD".!!.trim
    } catch { case e: Exception => "unknown" }))
  ),
  buildInfoPackage := "app"
)

TwirlKeys.templateImports ++= Seq(
  "com.madgag.github.Implicits._",
  "lib.actions.Requests._"
)

routesImport ++= Seq("lib._","com.madgag.github._","controllers.Binders._","org.eclipse.jgit.lib.ObjectId")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)

libraryDependencies ++= Seq(
  cache,
  filters,
  "com.madgag" %% "play-git-hub" % "1.1",
  "com.typesafe.akka" %% "akka-agent" % "2.3.2",
  "org.webjars" % "bootstrap" % "3.3.4",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24",
  "org.webjars.bower" % "octicons" % "2.2.3",
  "org.webjars.bower" % "timeago" % "1.4.1",
  "org.webjars.bower" % "typeahead.js" % "0.11.1",
  "org.webjars.bower" % "typeahead.js-bootstrap3.less" % "0.2.3",
  "org.webjars.npm" % "handlebars" % "3.0.3",
  "com.lihaoyi" %% "fastparse" % "0.2.1",
  "com.tristanhunt" %% "knockoff" % "0.8.3",
  "org.jsoup" % "jsoup" % "1.8.2",
  "com.github.nscala-time" %% "nscala-time" % "2.0.0",
  "com.netaporter" %% "scala-uri" % "0.4.7",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
  "org.scalatestplus" %% "play" % "1.4.0-M3",
  "com.amazonaws" % "aws-java-sdk-ses" % "1.9.37",
  "com.sun.mail" % "javax.mail" % "1.5.3"
)

sources in (Compile,doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
