package epam.idobrovolskiy.wikipedia.analysis

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter, PrintWriter}

import epam.idobrovolskiy.wikipedia.analysis.attardi.{AttardiWikiDocumentParsingStrategy, AttardiWikiDocumentProducer}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.collection.immutable.BitSet

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object WikiDocumentPreprocessor {
  def preprocessStats(inputPath: String, dest: PreprocessingDestination.Value, destPath: String = DefaultOutputFilePath) = {
    val docProducer = new AttardiWikiDocumentProducer(AttardiWikiDocumentParsingStrategy.ToBasicStats)

    //  val s = "doc with max body=[" + docProducer.getDocuments(path)
    //  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

    val documents = docProducer.getDocuments(inputPath)

    val destBitset = BitSet.fromBitMask(Array(dest.id.toLong))

    val printToStdout = destBitset.contains(PreprocessingDestination.Stdout.id)

    if (destBitset.contains(PreprocessingDestination.Hdfs.id))
      withHdfsFileName(destPath, documents)((bw, wd) => {
        val docString = wd.toString

        if (printToStdout)
          println(docString)

        bw.write(docString)
        bw.newLine()
      })
    else if (destBitset.contains(PreprocessingDestination.LocalFs.id))
      withFileName(destPath, documents)((pw, wd) => {
        val docString = wd.toString

        if (printToStdout)
          println(docString)

        pw.println(docString)
      })
  }

  private def withFileName[T](fname: String, ss: Seq[T])(op: (PrintWriter, T) => Unit) = {
    val writer = new PrintWriter(fname)
    try
        for (v <- ss)
          op(writer, v)

    finally
      writer.close()
  }

  lazy val initializeHdfs: FileSystem = {
    val configuration = new Configuration()
    configuration.set("fs.default.name", HdfsNameNodeHost)

    FileSystem.get(configuration)
  }

  private def withHdfsFileName[T](fname: String, ss: Seq[T])(op: (BufferedWriter, T) => Unit) = {
    val hdfs = initializeHdfs

    val file = new Path(HdfsRootPath + fname)
    if (hdfs.exists(file)) {
      hdfs.delete(file, true)
    }
    val osw: OutputStream = hdfs.create(file)

    val bw = new BufferedWriter(new OutputStreamWriter(osw, "UTF-8"))

    try
        for (v <- ss)
          op(bw, v)

    finally
      bw.close()

  }
}
