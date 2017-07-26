package epam.idobrovolskiy.wikipedia.analysis.common

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import epam.idobrovolskiy.wikipedia.analysis.{HdfsNameNodeHost, HdfsRootPath}
import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.SequenceFile.{Metadata, Writer}
import org.apache.hadoop.io.compress.DefaultCodec
import org.apache.hadoop.io.{IntWritable, SequenceFile, Text, Writable}

/**
  * Created by Igor_Dobrovolskiy on 26.07.2017.
  */
object HdfsUtils {
  private lazy val initializeHdfs: FileSystem = {
    val configuration = new Configuration()
    configuration.set("fs.default.name", HdfsNameNodeHost)

    FileSystem.get(configuration)
  }

  private def prepareHdfsAndFile(fname: String): (FileSystem, Path) = {
    val hdfs = initializeHdfs

    val file = new Path(HdfsRootPath + fname)
    if (hdfs.exists(file)) {
      hdfs.delete(file, true)
    }

    (hdfs, file)
  }

  def sinkToPlainFile[T](fname: String, ss: Seq[T])(convert: T => String) = {
    val (hdfs, path) = prepareHdfsAndFile(fname)

    val osw: OutputStream = hdfs.create(path)
    val bw = new BufferedWriter(new OutputStreamWriter(osw, "UTF-8"))

    try
        for (v <- ss) {
          bw.write(convert(v))
          bw.newLine()
        }

    finally
      bw.close()
  }

  private implicit class IntToWritable(i: Int) {
    def toWritable: IntWritable = {
      IntToWritable.intWritable.set(i)
      IntToWritable.intWritable
    }
  }

  private object IntToWritable {
    private val intWritable = new IntWritable()
  }

  private implicit class StringToWritable(s: String) {
    def toWritable: Text = {
      StringToWritable.textWritable.set(s)
      StringToWritable.textWritable
    }
  }

  private object StringToWritable {
    private val textWritable = new Text()
  }

  private def lazyInitializeWriter(writer: SequenceFile.Writer, hdfs: FileSystem, path: Path,
                                   kw: Writable, vw: Writable): SequenceFile.Writer =
    if (writer != null)
      writer
    else
      SequenceFile.createWriter(hdfs.getConf(),
        Writer.file(path),
        Writer.keyClass(kw.getClass()),
        Writer.valueClass(vw.getClass()),
        Writer.bufferSize(hdfs.getConf().getInt("io.file.buffer.size", 4096)),
        Writer.replication(hdfs.getDefaultReplication(path)),
        Writer.blockSize(1073741824),
        Writer.compression(SequenceFile.CompressionType.BLOCK, new DefaultCodec()),
        Writer.progressable(null),
        Writer.metadata(new Metadata()))

  def sinkToSequenceFile[T, K, V](fname: String, ss: Seq[T])(convert: T => (K, V)) = {
    val (hdfs, path) = prepareHdfsAndFile(fname)

    var writer: SequenceFile.Writer = null
    try {
      for (v <- ss) {
        val (kw: Writable, vw: Writable) = convert(v) match {
          case (ik: Int, sv: String) => (ik.toWritable, sv.toWritable)
          case (sk: String, sv: String) => (sk.toWritable, sv.toWritable)
          case (ik: Int, iv: Int) => (ik.toWritable, iv.toWritable)
          case (sk: String, iv: Int) => (sk.toWritable, iv.toWritable)
          case _ => throw new NoSuchElementException("No conversation to Writable is supported for the key or/and value type(s).")
        }
        writer = lazyInitializeWriter(writer, hdfs, path, kw, vw)

        writer.append(kw, vw)
      }
    }
    finally
      IOUtils.closeQuietly(writer)
  }
}
