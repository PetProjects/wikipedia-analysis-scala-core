package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.document.WikiDocumentHeader
import org.apache.hadoop.io.{BytesWritable, IntWritable}
import org.apache.spark.rdd.SequenceFileRDDFunctions

/**
  * Created by Igor_Dobrovolskiy on 28.07.2017.
  */
object WikiIndex extends App {

  //Just a test
  val data = spark.sparkContext
    .sequenceFile[IntWritable, BytesWritable](HdfsRootPath + DefaultFullTextOutputFilename + ".1")
    .map((r: (IntWritable, BytesWritable)) => WikiDocumentHeader(r._1.get(), "test", "test"))

//  val seq = new SequenceFileRDDFunctions(data).saveAsSequenceFile()
  data.saveAsTextFile(HdfsRootPath + "spark.test")

  spark.stop()
}
