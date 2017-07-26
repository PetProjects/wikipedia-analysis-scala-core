package epam.idobrovolskiy.wikipedia.analysis

import epam.idobrovolskiy.wikipedia.analysis.common.{FsUtils, HdfsUtils}
import epam.idobrovolskiy.wikipedia.analysis.document.WikiDocumentFullText

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object WikiDocumentPreprocessor {
  def preprocessStats(inputPath: String,
                      destTarget: DestinationTarget.Value,
                      destPath: String = DefaultOutputFilePath) =
    preprocess(inputPath,
      destTarget,
      destPath,
      serializeOnlyStats = true,
      new attardi.AttardiWikiDocumentProducer(DefaultPreprocessingStrategy))


  def preprocess(inputPath: String,
                 destTarget: DestinationTarget.Value,
                 destPath: String,
                 serializeOnlyStats: Boolean,
                 docProducer: WikiDocumentProducer) = {
    //  val s = "doc with max body=[" + docProducer.getDocuments(path)
    //  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

    val documents = docProducer.getDocuments(inputPath)
    val targetBitset = destTarget.id.toLong

    val printToStdout =
      if ((targetBitset & DestinationTarget.Stdout.id) != 0)
        (wd: WikiDocument) => {
//          if(wd.id % 37 == 0) {
//            val runtime = Runtime.getRuntime
//
//            val sb = new StringBuilder
//            val maxMemory = runtime.maxMemory
//            val allocatedMemory = runtime.totalMemory
//            val freeMemory = runtime.freeMemory
//            val mbBytes = 1024 * 1024
//
//            print(s"${wd.id}  [alloc: ${allocatedMemory / mbBytes}; free: ${freeMemory / mbBytes}; max: ${maxMemory / mbBytes}]")
//          }

          print(s"\r${wd.id}     ")
        }
      else
        (_: WikiDocument) => {}

    implicit class FileEntry(wd: WikiDocument) {
      def toFileEntry = wd match {
        case d: WikiDocumentFullText if !serializeOnlyStats =>
          d.toString + "\n\n" + d.fullText.mkString("\n")
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

    if ((targetBitset & DestinationTarget.HdfsPlainFile.id) != 0)
      HdfsUtils.sinkToPlainFile(destPath, documents)(convertToFileEntry)
    else if ((targetBitset & DestinationTarget.HdfsSequenceFile.id) != 0)
      HdfsUtils.sinkToSequenceFile(destPath, documents)(convertToIdAndFileEntry)
    else if ((targetBitset & DestinationTarget.LocalFs.id) != 0)
      FsUtils.sinkToFile(destPath, documents)(convertToFileEntry)
    else
      for (wd <- documents) printToStdout(wd)
  }

}
