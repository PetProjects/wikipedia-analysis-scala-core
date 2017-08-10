package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending._
import epam.idobrovolskiy.wikipedia.trending.preprocessing.PreprocessingTarget

import scala.annotation.tailrec

/**
  * Created by hp on 29.06.2017.
  */

object WikiPrepArgsParser {
  val usage =
    s"$AppName - $AppVersion" +
    """
      |Usage:
      | WikiPrep [--extract-plaintext [{target folder for plaintext files} [{wiki dumps path}]]]
      |     [--full-text] [--no-stdout] [--to-local-file] [--to-hdfs-plain-file]
      |     [--to-hdfs-seq-file]
      |     [{plain text file or dir name, is overridden by target path
      |                  for --extract-plaintext option once specified}]
    """.stripMargin

  private case class WikiPrepArgumentsRaw (
                                            var path: String = DefaultInputWikiDumpFilename,
                                            var targetBitset: Int = DefaultTarget.id,
                                            var fullText: Boolean = false,
                                            var extractToPath: String = DefaultPathForPlainTextExtraction,
                                            var extractFromPath: String = DefaultWikipediaDumpFilesPath,
                                            var extractPlainText: Boolean = false
  ) {
    def toWikiPrepArguments(convertTarget: Int => PreprocessingTarget.Value): WikiPrepArgs =
      WikiPrepArgs(path,
        convertTarget(targetBitset),
        fullText,
        extractToPath,
        extractFromPath,
        extractPlainText)
  }

  @tailrec
  private def parseOptions(argList: List[String], res: WikiPrepArgumentsRaw): Option[WikiPrepArgumentsRaw] = {
    def isSwitch(s: String) = s.startsWith("--")

    argList match {
      case Nil =>
        Some(res)
      case "--no-stdout" :: tail => {
        res.targetBitset = res.targetBitset & (-1 - PreprocessingTarget.Stdout.id)
        parseOptions(tail, res)
      }
      case "--to-local-file" :: tail => {
        res.targetBitset = res.targetBitset | PreprocessingTarget.LocalFs.id
        parseOptions(tail, res)
      }
      case "--to-hdfs-plain-file" :: tail => {
        res.targetBitset = res.targetBitset | PreprocessingTarget.HdfsPlainFile.id
        parseOptions(tail, res)
      }
      case "--to-hdfs-seq-file" :: tail => {
        res.targetBitset = res.targetBitset | PreprocessingTarget.HdfsSequenceFile.id
        parseOptions(tail, res)
      }
      case "--full-text" :: tail => {
        res.fullText = true
        parseOptions(tail, res)
      }
      case "--help" :: _ =>
        None
      case "--extract-plaintext" :: outputpath :: inputpath :: tail
        if (! isSwitch(outputpath) ) && (! isSwitch(inputpath)) => {
          res.extractFromPath = inputpath
          res.extractToPath = outputpath
          res.extractPlainText = true

          parseOptions(tail, res)
        }
      case "--extract-plaintext" :: outputpath :: tail
        if ! isSwitch(outputpath) => {
          res.extractToPath = outputpath
          res.extractPlainText = true

          parseOptions(tail, res)
        }
      case "--extract-plaintext" :: tail => {
        res.extractPlainText = true
        parseOptions(tail, res)
      }
      case path :: option :: _ if isSwitch(option) => {
        res.path = path
        parseOptions(argList.tail, res)
      }
      case path :: Nil if ! isSwitch(path) => {
        res.path = path
        Some(res)
      }
      case option :: _ =>
        println("Unknown option: " + option); None
    }
  }

  private def isThereTargetForBitset(targetBitset: Int) =
    PreprocessingTarget.values.map(_.id).contains(targetBitset)

  private def targetForBitset(targetBitset: Int): Option[PreprocessingTarget.Value] =
    PreprocessingTarget.values.find(_.id == targetBitset)

  def parse(args: Array[String]): Option[WikiPrepArgs] =
    parseOptions(args.toList, WikiPrepArgumentsRaw()) match {
      case Some(args: WikiPrepArgumentsRaw)
        if targetForBitset(args.targetBitset) != None =>
          Some(args.toWikiPrepArguments(x => targetForBitset(x).get))

      case _ => printUsage(); None
    }

  def printUsage() = println(usage)
}
