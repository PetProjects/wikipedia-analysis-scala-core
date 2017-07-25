import java.io.{BufferedWriter, OutputStream, OutputStreamWriter, PrintWriter}

import epam.idobrovolskiy.wikipedia.analysis.WikiDocumentWithBasicStats
import epam.idobrovolskiy.wikipedia.analysis.attardi._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

/**
  * Created by hp on 29.06.2017.
  */

object Main extends App {
  val DefaultFilePath = "wiki_small"
  val HdfsNameNodeHost = "hdfs://sandbox.hortonworks.com:8020"
  val HdfsRootPath = "/user/idobrovolskiy/wikipedia-trending/"

  def withFileName(fname: String)(op: PrintWriter => Unit) = {
    val writer = new PrintWriter(fname)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

  def initializeHdfs(): FileSystem = {
    val configuration = new Configuration()
    configuration.set("fs.default.name", HdfsNameNodeHost)

    FileSystem.get(configuration)
  }

  def withHdfsFileName[T](fname: String, ss: Seq[T])(op: (BufferedWriter, T) => Unit) = {
    val hdfs = initializeHdfs()

    val file = new Path(HdfsRootPath + fname)
    if (hdfs.exists(file)) {
      hdfs.delete(file, true)
    }
    val osw: OutputStream = hdfs.create(file)

    val bw = new BufferedWriter(new OutputStreamWriter(osw, "UTF-8"))

    try {
      for (v <- ss) {
        op(bw, v)
      }
    }
    finally {
      bw.close()
    }
  }

  //TODO: Support CLI params for processing opts

  val docProducer = new AttardiWikiDocumentProducer(AttardiWikiDocumentParsingStrategy.ToBasicStats)
  val path = if (args.length > 0) args(0) else DefaultFilePath

//  val s = docProducer.getDocuments(path).mkString("\n")
//
//  //  val s = "doc with max body=[" + docProducer.getDocuments(path)
//  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"
//
//  println(s)
//
//  withFileName("out.txt")(_.println(s))

  withHdfsFileName("out.txt", docProducer.getDocuments(path))((bw, wd) => {
    val docString = wd.toString

    println(docString)

    bw.write(docString)
    bw.newLine()
  })
}
