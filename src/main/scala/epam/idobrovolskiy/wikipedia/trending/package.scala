package epam.idobrovolskiy.wikipedia

import epam.idobrovolskiy.wikipedia.trending.time.extractor.stack.{BasicStackedDatesExtractor, InAndYearExtractor, InYearExtractor, OfYearExtractor}
import epam.idobrovolskiy.wikipedia.trending.tokenizer.StopWordsTokenizer
import org.apache.hadoop.io.{IntWritable, Text}

package object trending extends scala.AnyRef {
  val AppName = "wikipedia-trending"

  val DefaultTokenizer = new StopWordsTokenizer
  val TopTokenCount = 10

  val DefaultInputWikiDumpFilename = "wiki_small"
  val DefaultPrepHeaderFilename = "wiki_prep_headers"
  val DefaultPrepFullFilename = "wiki_prep_full"
  val DefaultDateCitationsFileName = "wiki_date_citations"
  val DefaultDateIndexFileName = "wiki_index_dates"
  val DefaultDocIndexFileName = "wiki_index_docs"

  val DefaultTarget = preprocessing.PreprocessingTarget.Stdout
  val DefaultPathForPlainTextExtraction = "./data/out"
  val DefaultWikipediaDumpFilesPath = "./data/in"
  val DefaultPlainTextExtractor = preprocessing.attardi.AttardiPlainTextExtractor

  val HdfsNameNodeHost = "hdfs://sandbox.hortonworks.com:8020"
  val HdfsRootPath = "/user/idobrovolskiy/wikipedia-trending/"

  val PreprocessedFileHeaderBodyDelimiter = "\n\n"
  type PreprocessedSequenceFileKeyType = IntWritable
  type PreprocessedSequenceFileValueType = Text

  val DefaultDatesExtractor = new BasicStackedDatesExtractor //any later mixin (trait) has higher priority, i.e. if both matched single date, later only would have effect on both extract() and extractWithCitations() results.
    with InYearExtractor
    with InAndYearExtractor
    with OfYearExtractor

  lazy val spark = common.SparkUtils.sparkSession
}