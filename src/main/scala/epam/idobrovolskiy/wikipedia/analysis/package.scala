package epam.idobrovolskiy.wikipedia

package object analysis extends scala.AnyRef {
  val DefaultInputFilePath = "wiki_small"
  val DefaultOutputFilePath = "wiki_stats"

  val HdfsNameNodeHost = "hdfs://sandbox.hortonworks.com:8020"
  val HdfsRootPath = "/user/idobrovolskiy/wikipedia-trending/"
}