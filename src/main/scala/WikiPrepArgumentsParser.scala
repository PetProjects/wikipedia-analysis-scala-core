import epam.idobrovolskiy.wikipedia.analysis.{DefaultInputFilePath, DestinationTarget}

/**
  * Created by hp on 29.06.2017.
  */

object WikiPrepArgumentsParser {
  val usage =
    """
      | Usage: WikiPrep [--no-stdout] [--to-local-file] [--to-hdfs-plain-file] [--to-hdfs-seq-file] fileOrDirName
    """.stripMargin

  private def parseOptions(argList: List[String], res: (String, Int)): Option[(String, Int)] = {
    def isSwitch(s: String) = s.startsWith("--")

    argList match {
      case Nil => Some(res)
      case "--no-stdout" :: tail =>
        parseOptions(tail, (res._1, res._2 & (-1 - DestinationTarget.Stdout.id)))
      case "--to-local-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.LocalFs.id))
      case "--to-hdfs-plain-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.HdfsPlainFile.id))
      case "--to-hdfs-seq-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.HdfsSequenceFile.id))
      case option :: Nil =>
        Some(option, res._2)
      case option :: tail =>
        println("Unknown option " + option)
        None
    }
  }

  def parse(args: Array[String]): Option[(String, DestinationTarget.Value)] =
    parseOptions(args.toList, DefaultInputFilePath -> DestinationTarget.StdoutAndLocalFs.id) match {
      case Some((path, targetBitset)) => Some((path, DestinationTarget.values.find(_.id == targetBitset).get))
      case None => printUsage(); None
    }

  def printUsage() = println(usage)
}
