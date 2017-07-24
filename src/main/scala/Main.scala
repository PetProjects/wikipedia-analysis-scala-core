import java.io.PrintWriter

import epam.idobrovolskiy.wikipedia.analysis.WikiDocumentWithBasicStats
import epam.idobrovolskiy.wikipedia.analysis.attardi._

/**
  * Created by hp on 29.06.2017.
  */

object Main extends App {
  val DefaultFilePath = "wiki_small"

  def printToFileName(fname: String)(op: PrintWriter => Unit) = {
    val writer = new PrintWriter(fname)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

  //TODO: Support CLI params for processing opts

  val docProducer = new AttardiWikiDocumentProducer(AttardiWikiDocumentParsingStrategy.ToBasicStats)
  val path = if (args.length > 0) args(0) else DefaultFilePath

  val s =  docProducer.getDocuments(path).mkString("\n")

  //  val s = "doc with max body=[" + docProducer.getDocuments(path)
  //    .collect { case x: WikiDocumentWithBasicStats => x }.maxBy(_.body.BodyLinesCount) + "]"

  println(s)

  printToFileName("out.txt")(_.println(s))
}
