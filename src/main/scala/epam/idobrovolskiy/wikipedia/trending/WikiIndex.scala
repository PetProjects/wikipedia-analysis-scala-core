package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.indexing.WikiDocumentIndexer
import org.apache.spark.sql.{DataFrame, SaveMode}

/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object WikiIndex extends App {

  val df: DataFrame = WikiDocumentIndexer.getPreprocessedSequenceFile

  //df.printSchema()

  //  df.write.mode(SaveMode.Overwrite).parquet(HdfsRootPath + "wiki_header.parquet")

  df
    .coalesce(1)
    .write
    .mode(SaveMode.Overwrite)
    .format("com.databricks.spark.csv")
    .option("header", "true")
    .save(HdfsRootPath + "wiki_header.csv")

  spark.stop()
}
