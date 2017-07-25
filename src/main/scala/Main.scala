import epam.idobrovolskiy.wikipedia.analysis._

/**
  * Created by hp on 29.06.2017.
  */

object Main extends App {
  //TODO: Support CLI params for processing opts

  val path = if (args.length > 0) args(0) else DefaultInputFilePath
  WikiDocumentPreprocessor.preprocessStats(path, PreprocessingDestination.StdoutAndHdfs)
}
