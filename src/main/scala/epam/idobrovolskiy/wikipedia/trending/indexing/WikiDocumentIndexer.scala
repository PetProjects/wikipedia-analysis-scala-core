package epam.idobrovolskiy.wikipedia.trending.indexing

import epam.idobrovolskiy.wikipedia.trending.common.SparkUtils
import epam.idobrovolskiy.wikipedia.trending.{PreprocessedFileHeaderBodyDelimiter => BodyDelimiter, PreprocessedSequenceFileKeyType => PSFKT, PreprocessedSequenceFileValueType => PSFVT, _}
import org.apache.spark.sql.functions.{explode, udf}
import org.apache.spark.sql.types.{StructField, _}
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

  val DateIndexFileSchema = StructType(
    Array(
      StructField("wiki_date", LongType),
      StructField("wiki_id", IntegerType)
    )
  )

  val DocsIndexFileSchema = StructType(
    Array(
      StructField("wiki_id", IntegerType),
      StructField("wiki_title", StringType),
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

  def getPreprocessedSequenceFile(fname: String = DefaultPrepFullFilename): DataFrame = {
    val data = spark.sparkContext
      .sequenceFile[PSFKT, PSFVT](HdfsRootPath + fname)
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
        .toSeq
    }

    df.select('id, explode(extractDatesAndCitations('id, 'body)) as "dates_citations")
      .limit(1000)
  }

  private def extractDates(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val extractDates = udf { (body: String) =>
      datesExtractor.extract(body)
        .map(_.serialize)
        .toSeq
    }

    df.select(
      explode(extractDates('body)) as DateIndexFileSchema(0).name,
      'id as DateIndexFileSchema(1).name
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
      'id as DocsIndexFileSchema(0).name,
      'title as DocsIndexFileSchema(1).name,
      'url as DocsIndexFileSchema(2).name,
      buildTopTokens('body) as DocsIndexFileSchema(3).name
    )
  }

  private def docsIndexDebugInfo(df: DataFrame, docsIndex: DataFrame) = {
    docsIndex.printSchema()
    docsIndex.show(20)

    import docsIndex.sqlContext.implicits._

    val tokensMapToString = udf { (map: Map[String, Int]) => map.toString }

    SparkUtils.saveAsCsv(
      docsIndex
        .limit(1000)
        .select(
          'wiki_id,
          'wiki_url,
          tokensMapToString('top_tokens)
        ),
      DefaultDocIndexFileName + ".debug")
  }

  private def buildDateIndex(df: DataFrame, debug: Boolean): Unit = {
    val datesIndex = extractDates(df)

    SparkUtils.saveAsParquet(datesIndex, DefaultDateIndexFileName)

    if (debug)
      datesIndexDebugInfo(df, datesIndex)
  }

  private def buildDocIndex(df: DataFrame, debug: Boolean): Unit = {
    val docsIndex = extractDocs(df)

    SparkUtils.saveAsParquet(docsIndex, DefaultDocIndexFileName)

    if (debug)
      docsIndexDebugInfo(df, docsIndex)
  }

  def buildIndex(debug: Boolean = false): Unit = {
    val df: DataFrame = WikiDocumentIndexer.getPreprocessedSequenceFile()

    buildDateIndex(df, debug)

    buildDocIndex(df, debug)
  }

  def getDateIndexFile(fname: String = DefaultDateIndexFileName): DataFrame = {
    spark.sqlContext.read.parquet(HdfsRootPath + fname)
  }

  def getDocIndexFile(fname: String = DefaultDocIndexFileName): DataFrame = {
    spark.sqlContext.read.parquet(HdfsRootPath + fname)
  }

  val DRDocsIndexFileSchema = StructType(
    Array(
      StructField("wiki_id", IntegerType),
      StructField("wiki_title", IntegerType),
      StructField("wiki_url", StringType),
      StructField("dates",
        StructType(Array(
          StructField("min_date", LongType),
          StructField("max_date", LongType),
          StructField("date_range",
            ArrayType(
              StructType(
                Array(
                  StructField("from", LongType),
                  StructField("to", LongType)
                )
              )
            )
          )
        ))
      ),
      StructField("top_tokens", MapType(StringType, IntegerType))
    )
  )

  case class DateRangeStruct(from: Long, to: Long)
  case class DatesStruct(min_date: Option[Long], max_date: Option[Long], date_range: Seq[DateRangeStruct])

  private def extractDrDocs(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val buildTopTokens = udf { (body: String) =>
      DefaultTokenizer.getTopNTokens(body.split('\n'))
    }

    val extractDates = udf { (body: String) =>
      val dates = datesExtractor.extract(body)
      val serDates = dates.map(_.serialize)

      DatesStruct(
        if (serDates.isEmpty) None else Some(serDates.min),
        if (serDates.isEmpty) None else Some(serDates.max),
        serDates.map(dSer => DateRangeStruct(dSer, dSer)).toSeq //TODO: rework into expanding real ranges,
        // e.g. WikiDate(None,None,Some(1970),None, None, Some(true)) =>
        //    WikiDate(Some(1),Some(Month.January),Some(1970),None, None, Some(true)) .. WikiDate(Some(31),Some(Month.December),Some(1970),None, None, Some(true))
      )
    }

    df.select(
      'id as DRDocsIndexFileSchema(0).name,
      'title as DRDocsIndexFileSchema(1).name,
      'url as DRDocsIndexFileSchema(2).name,
      extractDates('body) as "dates",
      buildTopTokens('body) as "top_tokens"
    )
  }

  private def drDocsIndexDebugInfo(df: DataFrame, drDocsIndex: DataFrame) = {
    drDocsIndex.printSchema()
    drDocsIndex.show(20)

    //    import drDocsIndex.sqlContext.implicits._
    //
    //    val tokensMapToString = udf { (map: Map[String, Int]) => map.toString }
    //
    //    SparkUtils.saveAsCsv(
    //      drDocsIndex
    //        .limit(1000)
    //        .select(
    //          'wiki_id,
    //          'wiki_url,
    //          tokensMapToString('top_tokens)
    //        ),
    //      DefaultDrDocIndexFileName + ".debug")
  }

  private def buildDrDocIndex(df: DataFrame, debug: Boolean): Unit = {
    val drDocsIndex = extractDrDocs(df)

    SparkUtils.saveAsParquet(drDocsIndex, DefaultDrDocIndexFileName)

    if (debug)
      drDocsIndexDebugInfo(df, drDocsIndex)
  }

  def buildDateRangesDocIndex(debug: Boolean = false): Unit = {
    val df: DataFrame = WikiDocumentIndexer.getPreprocessedSequenceFile(DefaultPrepFullFilename)

    buildDrDocIndex(df, debug)
  }

  def getDrDocIndexFile(fname: String = DefaultDrDocIndexFileName): DataFrame = {
    spark.sqlContext.read.parquet(HdfsRootPath + fname)
  }
}
