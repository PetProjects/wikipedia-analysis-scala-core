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

    //    val destBitset = BitSet.fromBitMask(Array(dest.id.toLong))
    val destBitset = dest.id.toLong

    val printToStdout = (destBitset & PreprocessingDestination.Stdout.id) != 0

    def doPrintToStdout(wd: WikiDocument) =
      if (printToStdout) print(s"\r${wd.id}        ") //println(docString)

    if ((destBitset & PreprocessingDestination.Hdfs.id) != 0)
      withHdfsFileName(destPath, documents)((bw, wd) => {
        val docString = wd.toString

        doPrintToStdout(wd)

        bw.write(docString)
        bw.newLine()
      })
    else if ((destBitset & PreprocessingDestination.LocalFs.id) != 0)
      withFileName(destPath, documents)((pw, wd) => {
        val docString = wd.toString

        doPrintToStdout(wd)

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
