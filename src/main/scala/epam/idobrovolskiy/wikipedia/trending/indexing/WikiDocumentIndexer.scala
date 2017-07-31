package epam.idobrovolskiy.wikipedia.trending.indexing

import epam.idobrovolskiy.wikipedia.trending.{
  PreprocessedSequenceFileKeyType => PSFKT,
  PreprocessedSequenceFileValueType => PSFVT,
  PreprocessedFileHeaderBodyDelimiter => BodyDelimiter,
  _}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object WikiDocumentIndexer {
  val PreprocessedFileSchema = StructType(
    Array(
      StructField("id", IntegerType),
      StructField("title", StringType),
      StructField("url", StringType),
      StructField("body", StringType))
  )

  private def sequenceKeyValueToPreprocessedKeyValue(r: (PSFKT, PSFVT)): (Int, String, String) = {
    val (header, body) = r._2.toString.span(_ != BodyDelimiter(0))

    (r._1.get(), header, body.drop(BodyDelimiter.length))
  }

  private def preprocessedKeyValueToRow(r: (Int, String, String)): Row = {
    val titleStarkMark = "title="
    val urlStarkMark = "url="

    val id = r._1
    val header = r._2
    val titleStart = header.indexOf(titleStarkMark) + titleStarkMark.length
    val titleEnd = header.indexOf(',', titleStart)
    val title = header.substring(titleStart, titleEnd)
    val urlStart = header.indexOf(urlStarkMark, titleEnd) + urlStarkMark.length
    val urlEnd = header.indexOf(',', urlStart)
    val url = header.substring(urlStart, urlEnd)
    val body = r._3

    Row(id, title, url, body)
  }

  def getPreprocessedSequenceFile: DataFrame = {
    val data = spark.sparkContext
      .sequenceFile[PSFKT, PSFVT](HdfsRootPath + DefaultFullTextOutputFilename)
      .map(sequenceKeyValueToPreprocessedKeyValue)
      .map(preprocessedKeyValueToRow)

    spark.sqlContext.createDataFrame(data, PreprocessedFileSchema)
  }
}
