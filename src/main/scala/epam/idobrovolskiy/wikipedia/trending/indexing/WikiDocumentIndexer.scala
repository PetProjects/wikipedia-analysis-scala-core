package epam.idobrovolskiy.wikipedia.trending.indexing

import epam.idobrovolskiy.wikipedia.trending.common.SparkUtils
import epam.idobrovolskiy.wikipedia.trending.{
  PreprocessedFileHeaderBodyDelimiter => BodyDelimiter,
  PreprocessedSequenceFileKeyType => PSFKT,
  PreprocessedSequenceFileValueType => PSFVT, _
}
import org.apache.spark.sql.functions.{explode, udf}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row}

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

  //TODO: try if storing data this way is more efficient than split into two tables of "dates" and "docs" indexes
  //  val FirstLastDateIndexFileSchema = StructType(
  //    Array(
  //      StructField("firstdate", LongType),
  //      StructField("lastdate", LongType),
  //      StructField("wiki_title", IntegerType),
  //      StructField("wiki_id", IntegerType),
  //      StructField("wiki_url", StringType),
  //      StructField("dates", ArrayType(LongType)),
  //      StructField("top_tokens", MapType(StringType, IntegerType))
  //    )
  //  )

  val DateIndexFileSchema = StructType(
    Array(
      StructField("wiki_date", LongType),
      StructField("wiki_id", IntegerType)
    )
  )

  val DocsIndexFileSchema = StructType(
    Array(
      StructField("wiki_id", IntegerType),
      StructField("wiki_title", IntegerType),
      StructField("wiki_url", StringType),
      StructField("top_tokens", MapType(StringType, IntegerType))
    )
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
      .sequenceFile[PSFKT, PSFVT](HdfsRootPath + DefaultPrepFullFilename + ".1") //TODO: remove ".1"
      .map(sequenceKeyValueToPreprocessedKeyValue)
      .map(preprocessedKeyValueToRow)

    spark.sqlContext.createDataFrame(data, PreprocessedFileSchema)
  }

  val datesExtractor = DefaultDatesExtractor

  private def extractDateCitationsForDebug(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val extractDatesAndCitations = udf { (id: Int, body: String) =>
      datesExtractor.extractWithCitations(id, body)
        .map { case (date, citation) => s"$date => $citation" }
    }

    df.select($"id", explode(extractDatesAndCitations('id, 'body)) as "dates_citations")
      .limit(1000)
  }

  private def extractDates(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val extractDates = udf { (body: String) =>
      datesExtractor.extract(body).map(_.serialize)
    }

    df.select(
      explode(extractDates('body)) as DateIndexFileSchema(0).name,
      $"id" as DateIndexFileSchema(1).name
    )

  }

  private def datesIndexDebugInfo(df: DataFrame, datesIndex: DataFrame) = {
    df.printSchema()
    df.show(10)

    val dateCitationsForDebug = extractDateCitationsForDebug(df)

    dateCitationsForDebug.printSchema()
    dateCitationsForDebug.show(10)

    SparkUtils.saveAsCsv(dateCitationsForDebug, DefaultDateCitationsFileName)

    datesIndex.printSchema()
    datesIndex.show(10)
  }

  private def extractDocs(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val buildTopTokens = udf { (body: String) =>
      DefaultTokenizer.getTopNTokens(body.split('\n'))
    }

    df.select(
      df(PreprocessedFileSchema(0).name) as DocsIndexFileSchema(0).name,
      df(PreprocessedFileSchema(1).name) as DocsIndexFileSchema(1).name,
      df(PreprocessedFileSchema(2).name) as DocsIndexFileSchema(2).name,
      buildTopTokens('body) as DocsIndexFileSchema(3).name
    )
  }

  private def docsIndexDebugInfo(df: DataFrame, docsIndex: DataFrame) = {
    docsIndex.printSchema()
    docsIndex.show(20)

    import docsIndex.sqlContext.implicits._

    val tokensMapToString = udf { (map: Map[String, Int]) => map.toString }

    SparkUtils.saveAsCsv(
      docsIndex.limit(1000).select(tokensMapToString($"top_tokens")),
      DefaultDocIndexFileName + ".debug")
  }

  private def buildDateIndex(df: DataFrame, debug: Boolean) = {
    val datesIndex = extractDates(df)

    SparkUtils.saveAsParquet(datesIndex, DefaultDateIndexFileName)

    if (debug)
      datesIndexDebugInfo(df, datesIndex)
  }

  private def buildDocIndex(df: DataFrame, debug: Boolean) = {
    val docsIndex = extractDocs(df)

    SparkUtils.saveAsParquet(docsIndex, DefaultDocIndexFileName)

    if (debug)
      docsIndexDebugInfo(df, docsIndex)
  }

  def buildIndex(debug: Boolean = false) = {
    val df: DataFrame = WikiDocumentIndexer.getPreprocessedSequenceFile

    buildDateIndex(df, debug)

    buildDocIndex(df, debug)
  }

  def getDateIndexFile: DataFrame = {
    spark.sqlContext.read.parquet(HdfsRootPath + DefaultDateIndexFileName)
  }

  def getDocIndexFile: DataFrame = {
    spark.sqlContext.read.parquet(HdfsRootPath + DefaultDocIndexFileName)
  }
}
