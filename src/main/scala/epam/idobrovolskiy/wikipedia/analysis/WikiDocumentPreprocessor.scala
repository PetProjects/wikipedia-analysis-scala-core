package epam.idobrovolskiy.wikipedia.analysis

import epam.idobrovolskiy.wikipedia.analysis.common.{FsUtils, HdfsUtils}

/**
  * Created by Igor_Dobrovolskiy on 25.07.2017.
  */
object WikiDocumentPreprocessor {
  private val docProducer = new attardi.AttardiWikiDocumentProducer(DefaultPreprocessingStrategy)

  def preprocessStats(inputPath: String, destTarget: DestinationTarget.Value, destPath: String = DefaultOutputFilePath) = {
    //  val s = "doc with max body=[" + docProducer.getDocuments(path)
    //  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

    val documents = docProducer.getDocuments(inputPath)

    val targetBitset = destTarget.id.toLong

    val printToStdout = (targetBitset & DestinationTarget.Stdout.id) != 0

    def doPrintToStdout(wd: WikiDocument) =
      if (printToStdout) print(s"\r${wd.id}        ") //println(docString)

    if ((targetBitset & DestinationTarget.HdfsPlainFile.id) != 0)
      HdfsUtils.sinkToPlainFile(destPath, documents)(wd => {
        doPrintToStdout(wd)
        wd.toString
      })
    else if ((targetBitset & DestinationTarget.HdfsSequenceFile.id) != 0)
      HdfsUtils.sinkToSequenceFile(destPath, documents)(wd => {
        doPrintToStdout(wd)
        (wd.id, wd.toString)
      })
    else if ((targetBitset & DestinationTarget.LocalFs.id) != 0)
      FsUtils.sinkToFile(destPath, documents)(wd => {
        doPrintToStdout(wd)
        wd.toString
      })
    else
      for(wd <- documents) doPrintToStdout(wd)
  }
}
