package epam.idobrovolskiy.wikipedia

package object trending extends scala.AnyRef {
  val AppName = "wikipedia-trending"

  val DefaultInputFilename = "wiki_small"
  val DefaultOutputFilename = "wiki_stats"
  val DefaultFullTextOutputFilename = "wiki_full"
  val DefaultTarget = preprocessing.PreprocessingTarget.Stdout
  val DefaultPathForPlainTextExtraction = "./data/out"
  val DefaultWikipediaDumpFilesPath = "./data/in"
  val DefaultPlainTextExtractor = preprocessing.attardi.AttardiPlainTextExtractor

  val HdfsNameNodeHost = "hdfs://sandbox.hortonworks.com:8020"
  val HdfsRootPath = "/user/idobrovolskiy/wikipedia-trending/"

  val spark = common.SparkUtils.sparkSession
}