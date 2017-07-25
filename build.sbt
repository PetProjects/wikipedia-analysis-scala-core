name := "wikipedia-analysis-scala-core"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
//libraryDependencies += "org.apache.lucene" % "lucene-analyzers" % "3.4.0"

resolvers += "Hortonworks Releases" at "http://repo.hortonworks.com/content/repositories/releases/"
resolvers += "Hortonworks Jetty Maven Repository" at "http://repo.hortonworks.com/content/repositories/jetty-hadoop/"

//resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository" //unix way
resolvers += "Local Maven Repository" at "file:///" + Path.userHome.absolutePath.replaceAllLiterally("\\", "/") + "/.m2/repository" //windows way

// https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-common
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.1.2.4.2.10-1"
