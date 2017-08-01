package epam.idobrovolskiy.wikipedia

import epam.idobrovolskiy.wikipedia.trending.time.date.PlainDatesExtractor
import org.apache.hadoop.io.{IntWritable, Text}

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

  val PreprocessedFileHeaderBodyDelimiter = "\n\n"
  type PreprocessedSequenceFileKeyType = IntWritable
  type PreprocessedSequenceFileValueType = Text

  val WikiIndexFileName = "wiki_index"

  val DefaultDatesExtractor = new PlainDatesExtractor

  lazy val spark = common.SparkUtils.sparkSession
}