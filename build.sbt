name := "wikipedia-trending"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

resolvers += "Hortonworks Releases" at "http://repo.hortonworks.com/content/repositories/releases/"
resolvers += "Hortonworks Jetty Maven Repository" at "http://repo.hortonworks.com/content/repositories/jetty-hadoop/"

//resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository" //unix way
resolvers += "Local Maven Repository" at "file:///" + Path.userHome.absolutePath.replaceAllLiterally("\\", "/") + "/.m2/repository" //windows way

libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.1.2.4.2.10-1"

libraryDependencies ++= {
  //2.1.0.2.6.0.3-8
  val sparkVer = "2.1.0"
  Seq(
    //"org.apache.spark" %% "spark-core" % sparkVer % "provided" withSources()
    "org.apache.spark" % "spark-core_2.11" % sparkVer % "provided" withSources(),
    "org.apache.spark" % "spark-sql_2.11" % sparkVer % "provided" withSources(),
    "org.apache.spark" % "spark-hive_2.11" % sparkVer % "provided" withSources()
  )
}