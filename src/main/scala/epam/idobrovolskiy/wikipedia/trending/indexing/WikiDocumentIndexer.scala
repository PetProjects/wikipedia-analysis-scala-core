package epam.idobrovolskiy.wikipedia.trending.indexing

import epam.idobrovolskiy.wikipedia.trending.{PreprocessedFileHeaderBodyDelimiter => BodyDelimiter, PreprocessedSequenceFileKeyType => PSFKT, PreprocessedSequenceFileValueType => PSFVT, _}
import org.apache.spark.sql.{DataFrame, Row, SaveMode}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.functions.{udf, explode}

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
      .sequenceFile[PSFKT, PSFVT](HdfsRootPath + DefaultFullTextOutputFilename + ".1") //TODO: remove ".1"
      .map(sequenceKeyValueToPreprocessedKeyValue)
      .map(preprocessedKeyValueToRow)

    spark.sqlContext.createDataFrame(data, PreprocessedFileSchema)
  }

  val datesExtractor = DefaultDatesExtractor

  def extractDates(df: DataFrame): DataFrame = {
    import df.sqlContext.implicits._

    val extractDatesAndCitations = udf { (id: Int, body: String) =>
      datesExtractor.extractWithCitations(id, body)
        .map { case (date, citation) => s"$date => $citation" }
    }

    df.select($"id", explode(extractDatesAndCitations('id, 'body)) as "dates_citations")
  }

  def buildIndex = {
    val df: DataFrame = WikiDocumentIndexer.getPreprocessedSequenceFile

    //df.printSchema()

    //  df.write.mode(SaveMode.Overwrite).parquet(HdfsRootPath + "wiki_header.parquet")

    //    df
    //      .coalesce(1)
    //      .write
    //      .mode(SaveMode.Overwrite)
    //      .format("com.databricks.spark.csv")
    //      .option("header", "true")
    //      .save(HdfsRootPath + "wiki_header.csv")

    val dfIndex = extractDates(df)

    dfIndex.printSchema()
    dfIndex.show(10)

//    dfIndex.write.mode(SaveMode.Overwrite).parquet(HdfsRootPath + WikiIndexFileName)

    dfIndex.coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .save(HdfsRootPath + "wiki_index.csv")
  }
}
