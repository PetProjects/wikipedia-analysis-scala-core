package epam.idobrovolskiy.wikipedia.trending.preprocessing

import epam.idobrovolskiy.wikipedia.trending._
import epam.idobrovolskiy.wikipedia.trending.cli.WikiPrepArgs
import epam.idobrovolskiy.wikipedia.trending.common.{FsUtils, HdfsUtils}
import epam.idobrovolskiy.wikipedia.trending.document.{WikiDocument, WikiDocumentFullText, WikiDocumentProducer}

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object WikiDocumentPreprocessor {
  def preprocessStats(args: WikiPrepArgs, destFilename: String) =
    preprocess(args, destFilename,
      new attardi.AttardiWikiDocumentProducer(
        attardi.AttardiWikiDocumentParsingStrategy.ToBasicStats)
    )


  def preprocess(args: WikiPrepArgs, destFilename: String,
                 docProducer: WikiDocumentProducer) =
  {
    //  val s = "doc with max body=[" + docProducer.getDocuments(path)
    //  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

    var docPath = args.path

    if(args.extractPlainText) {
      docPath = args.extractToPath
      DefaultPlainTextExtractor.extract(args.extractFromPath, docPath)
    }

    def documents = docProducer.getDocuments(docPath)
    val targetBitset = args.target.id.toLong

    val printToStdout =
      if ((targetBitset & PreprocessingTarget.Stdout.id) != 0)
        (wd: WikiDocument) => {
          if(wd.id % 37 == 0) {
            val runtime = Runtime.getRuntime

            val sb = new StringBuilder
            val maxMemory = runtime.maxMemory
            val allocatedMemory = runtime.totalMemory
            val freeMemory = runtime.freeMemory
            val mbBytes = 1024 * 1024

            print(s"\r${wd.id}  [alloc: ${allocatedMemory / mbBytes}; free: ${freeMemory / mbBytes}; max: ${maxMemory / mbBytes}]            ")
          }

//          print(s"\r${wd.id}     ")
        }
      else
        (_: WikiDocument) => {}

    implicit class FileEntry(wd: WikiDocument) {

      def toFileEntry = wd match {
        case d: WikiDocumentFullText if args.fullText =>
          d.toString + PreprocessedFileHeaderBodyDelimiter + d.fullText.mkString("\n")
        case d => d.toString
      }
    }

    val convertToFileEntry = (wd: WikiDocument) => {
      printToStdout(wd)
      wd.toFileEntry
    }

    val convertToIdAndFileEntry = (wd: WikiDocument) => {
      printToStdout(wd)
      (wd.id, wd.toFileEntry)
    }

    if ((targetBitset & PreprocessingTarget.HdfsPlainFile.id) != 0)
      HdfsUtils.sinkToPlainFile(destFilename, documents)(convertToFileEntry)
    else if ((targetBitset & PreprocessingTarget.HdfsSequenceFile.id) != 0)
      HdfsUtils.sinkToSequenceFile(destFilename, documents)(convertToIdAndFileEntry)
    else if ((targetBitset & PreprocessingTarget.LocalFs.id) != 0)
      FsUtils.sinkToFile(destFilename, documents)(convertToFileEntry)
    else
      for (wd <- documents) printToStdout(wd)
  }

}
