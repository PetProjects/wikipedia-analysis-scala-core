import sbt.Keys._

lazy val GatlingTest = config("gatling") extend Test

// This must be set to 2.11.11 because Gatling does not run on 2.12.2
scalaVersion in ThisBuild := "2.11.11"

unmanagedJars in Compile += file("../target/scala-2.11/wikipedia-trending_2.11-0.2.2.jar")

libraryDependencies ++= {
  //2.1.0.2.6.0.3-8
  val sparkVer = "2.1.0"
  Seq(
    "org.apache.spark" % "spark-core_2.11" % sparkVer % "provided",
    "org.apache.spark" % "spark-sql_2.11" % sparkVer % "provided",
    "org.apache.spark" % "spark-hive_2.11" % sparkVer % "provided"
  )
}

libraryDependencies += guice
libraryDependencies += "org.joda" % "joda-convert" % "1.8"
libraryDependencies += "net.logstash.logback" % "logstash-logback-encoder" % "4.9"

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.16"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0-M3" % Test
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.2.2" % Test
libraryDependencies += "io.gatling" % "gatling-test-framework" % "2.2.2" % Test

// The Play project itself
lazy val root = (project in file("."))
  .enablePlugins(Common, PlayScala, GatlingPlugin)
  .configs(GatlingTest)
  .settings(inConfig(GatlingTest)(Defaults.testSettings): _*)
  .settings(
    name := """play-scala-rest-api-example""",
    scalaSource in GatlingTest := baseDirectory.value / "/gatling/simulation"
  )

// Documentation for this project:
//    sbt "project docs" "~ paradox"
//    open docs/target/paradox/site/index.html
lazy val docs = (project in file("docs")).enablePlugins(ParadoxPlugin).
  settings(
    paradoxProperties += ("download_url" -> "https://example.lightbend.com/v1/download/play-rest-api")
  )
