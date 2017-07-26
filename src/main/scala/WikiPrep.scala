import epam.idobrovolskiy.wikipedia.analysis._

object WikiPrep extends App {
  WikiPrepArgumentsParser.parse(args) match {
    case Some((path, target)) => WikiDocumentPreprocessor.preprocessStats(path, target)
    case _ =>
  }
}
