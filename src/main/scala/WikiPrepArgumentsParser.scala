import epam.idobrovolskiy.wikipedia.analysis._

import scala.annotation.tailrec

/**
  * Created by hp on 29.06.2017.
  */

object WikiPrepArgumentsParser {
  val usage =
    """
      | Usage: WikiPrep [--full-text] [--no-stdout] [--to-local-file] [--to-hdfs-plain-file] [--to-hdfs-seq-file] fileOrDirName
    """.stripMargin

  @tailrec
  private def parseOptions(argList: List[String], res: WikiPrepArgumentsRaw): Option[WikiPrepArgumentsRaw] = {
    def isSwitch(s: String) = s.startsWith("--")

    argList match {
      case Nil =>
        Some(res)
      case "--no-stdout" :: tail =>
        parseOptions(tail,
          WikiPrepArgumentsRaw(res, targetBitset = res.targetBitset & (-1 - DestinationTarget.Stdout.id)))
      case "--to-local-file" :: tail =>
        parseOptions(tail,
          WikiPrepArgumentsRaw(res, targetBitset = res.targetBitset | DestinationTarget.LocalFs.id))
      case "--to-hdfs-plain-file" :: tail =>
        parseOptions(tail,
          WikiPrepArgumentsRaw(res, targetBitset = res.targetBitset | DestinationTarget.HdfsPlainFile.id))
      case "--to-hdfs-seq-file" :: tail =>
        parseOptions(tail,
          WikiPrepArgumentsRaw(res, targetBitset = res.targetBitset | DestinationTarget.HdfsSequenceFile.id))
      case "--full-text" :: tail =>
        parseOptions(tail,
          WikiPrepArgumentsRaw(res, fullText = true))
      case "--help" :: _ =>
        None
      case path :: option :: _ if isSwitch(option) =>
        parseOptions(argList.tail,
          WikiPrepArgumentsRaw(res, path = path))
      case path :: Nil if ! isSwitch(path) =>
        Some(WikiPrepArgumentsRaw(res, path = path))
      case option :: _ =>
        println("Unknown option " + option); None
    }
  }

  private def isThereTargetForBitset(targetBitset: Int) =
    DestinationTarget.values.map(_.id).contains(targetBitset)

  private def targetForBitset(targetBitset: Int): DestinationTarget.Value =
    DestinationTarget.values.find(_.id == targetBitset).get

  def parse(args: Array[String]): Option[WikiPrepArguments] =
    parseOptions(args.toList, WikiPrepArgumentsRaw()) match {
      case Some(args: WikiPrepArgumentsRaw)
        if isThereTargetForBitset(args.targetBitset) =>
          Some(WikiPrepArguments(args.path,
            targetForBitset(args.targetBitset),
            args.fullText))

      case _ => printUsage(); None
    }

  def printUsage() = println(usage)
}
