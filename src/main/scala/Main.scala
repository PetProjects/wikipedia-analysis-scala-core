import epam.idobrovolskiy.wikipedia.analysis.WikiDocumentWithBasicStats
import epam.idobrovolskiy.wikipedia.analysis.attardi._

/**
  * Created by hp on 29.06.2017.
  */

object Main extends App {
  val DefaultFilePath = "wiki_small"

  //TODO: Support CLI params for processing opts

  //val docProducer = new AttardiWikiDocumentProducer //plain/default strategy
  val docProducer = new AttardiWikiDocumentProducer()(AttardiWikiDocumentParsingStrategy.ToBasicStats)
  val path = if (args.length > 0) args(0) else DefaultFilePath

//  docProducer.getDocuments(path).foreach(x => println(x))

  println("doc with max body=[" + docProducer.getDocuments(path)
    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]")
}
