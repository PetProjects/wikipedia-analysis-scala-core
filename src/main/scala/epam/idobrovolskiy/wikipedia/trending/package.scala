package epam.idobrovolskiy.wikipedia

package object trending extends scala.AnyRef {
  val DefaultInputFilePath = "wiki_small"
  val DefaultOutputFilePath = "wiki_stats"
  val DefaultFullTextOutputFilePath = "wiki_full"
  val DefaultTarget = preprocessing.DestinationTarget.Stdout
  val DefaultPathForPlainTextExtraction = "./data/out"
  val DefaultWikipediaDumpFilesPath = "./data/in"
  val DefaultPlainTextExtractor = preprocessing.attardi.AttardiPlainTextExtractor

  val HdfsNameNodeHost = "hdfs://sandbox.hortonworks.com:8020"
  val HdfsRootPath = "/user/idobrovolskiy/wikipedia-trending/"
}