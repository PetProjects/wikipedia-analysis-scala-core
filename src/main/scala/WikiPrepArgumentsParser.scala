import epam.idobrovolskiy.wikipedia.analysis._

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
      case Nil =>
        Some(res)
      case "--no-stdout" :: tail =>
        parseOptions(tail, (res._1, res._2 & (-1 - DestinationTarget.Stdout.id)))
      case "--to-local-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.LocalFs.id))
      case "--to-hdfs-plain-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.HdfsPlainFile.id))
      case "--to-hdfs-seq-file" :: tail =>
        parseOptions(tail, (res._1, res._2 | DestinationTarget.HdfsSequenceFile.id))
      case "--help" :: _ =>
        None
      case path :: option :: _ if isSwitch(option) =>
        parseOptions(argList.tail, (path, res._2))
      case option :: Nil if ! isSwitch(option) =>
        Some(option, res._2)
      case option :: tail =>
        println("Unknown option " + option); None
    }
  }

  private def isThereTargetForBitset(targetBitset: Int) =
    DestinationTarget.values.map(_.id).contains(targetBitset)

  private def targetForBitset(targetBitset: Int): DestinationTarget.Value =
    DestinationTarget.values.find(_.id == targetBitset).get

  def parse(args: Array[String]): Option[(String, DestinationTarget.Value)] =
    parseOptions(args.toList, DefaultInputFilePath -> DefaultTarget.id) match {
      case Some((path, targetBitset)) if isThereTargetForBitset(targetBitset) =>
        Some((path, targetForBitset(targetBitset)))
      case _ => printUsage(); None
    }

  def printUsage() = println(usage)
}
