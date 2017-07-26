package epam.idobrovolskiy.wikipedia.analysis

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter, PrintWriter}

import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.SequenceFile.{Metadata, Writer}
import org.apache.hadoop.io.compress.DefaultCodec
import org.apache.hadoop.io.{IntWritable, SequenceFile, Text}

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object WikiDocumentPreprocessor {
  private val docProducer = new attardi.AttardiWikiDocumentProducer(DefaultPreprocessingStrategy)

  private lazy val initializeHdfs: FileSystem = {
    val configuration = new Configuration()
    configuration.set("fs.default.name", HdfsNameNodeHost)

    FileSystem.get(configuration)
  }

  private val sequenceFileKey: IntWritable = new IntWritable()
  private val sequenceFileValue: Text = new Text()

  def preprocessStats(inputPath: String, dest: PreprocessingDestination.Value, destPath: String = DefaultOutputFilePath) = {
    //  val s = "doc with max body=[" + docProducer.getDocuments(path)
    //  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

    val documents = docProducer.getDocuments(inputPath)

    val destBitset = dest.id.toLong

    val printToStdout = (destBitset & PreprocessingDestination.Stdout.id) != 0

    def doPrintToStdout(wd: WikiDocument) =
      if (printToStdout) print(s"\r${wd.id}        ") //println(docString)

    if ((destBitset & PreprocessingDestination.HdfsPlainFile.id) != 0)
      withHdfsPlainFile(destPath, documents)((bw, wd) => {
        val docString = wd.toString

        doPrintToStdout(wd)

        bw.write(docString)
        bw.newLine()
      })
    else if ((destBitset & PreprocessingDestination.HdfsSequenceFile.id) != 0)
      withHdfsSequenceFile(destPath, documents)((sfw, wd) => {
        val docString = wd.toString

        doPrintToStdout(wd)

        sequenceFileKey.set(wd.id)
        sequenceFileValue.set(docString)

        sfw.append(sequenceFileKey, sequenceFileValue)
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

  def prepareHdfsAndFile(fname: String) : (FileSystem, Path) = {
    val hdfs = initializeHdfs

    val file = new Path(HdfsRootPath + fname)
    if (hdfs.exists(file)) {
      hdfs.delete(file, true)
    }

    (hdfs, file)
  }

  private def withHdfsPlainFile[T](fname: String, ss: Seq[T])(op: (BufferedWriter, T) => Unit) = {
    val (hdfs, path) = prepareHdfsAndFile(fname)

    val osw: OutputStream = hdfs.create(path)
    val bw = new BufferedWriter(new OutputStreamWriter(osw, "UTF-8"))

    try
        for (v <- ss)
          op(bw, v)

    finally
      bw.close()
  }

  private def withHdfsSequenceFile[T](fname: String, ss: Seq[T])(op: (Writer, T) => Unit) = {
    val (hdfs, path) = prepareHdfsAndFile(fname)

    var writer: SequenceFile.Writer = null
    try {
      writer = SequenceFile.createWriter(hdfs.getConf(),
        Writer.file(path),
        Writer.keyClass(sequenceFileKey.getClass()),
        Writer.valueClass(sequenceFileValue.getClass()),
        Writer.bufferSize(hdfs.getConf().getInt("io.file.buffer.size", 4096)),
        Writer.replication(hdfs.getDefaultReplication(path)),
        Writer.blockSize(1073741824),
        Writer.compression(SequenceFile.CompressionType.BLOCK, new DefaultCodec()),
        Writer.progressable(null),
        Writer.metadata(new Metadata()))

      for (v <- ss)
        op(writer, v)
    }
    finally
      IOUtils.closeQuietly(writer)
  }
}
