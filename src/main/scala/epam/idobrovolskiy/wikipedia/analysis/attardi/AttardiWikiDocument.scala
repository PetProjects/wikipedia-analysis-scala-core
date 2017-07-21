package epam.idobrovolskiy.wikipedia.analysis.attardi

import epam.idobrovolskiy.wikipedia.analysis._

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
object AttardiWikiDocument {

  //<doc id="12" url="https://en.wikipedia.org/wiki?curid=12" title="Anarchism">
  //<doc id="307" url="https://en.wikipedia.org/wiki?curid=307" title="Abraham Lincoln">

  private val FirstLineRE = """<doc id="(\d+)" url="([\S\d\.\?\=\#]+)" title="([^\"]+)">""".r
  private val StartDocMark = "<doc "
  private val EndDocMark = "</doc>"

  private def parseHeader(header: String): WikiDocument =
    header match {
      case FirstLineRE(id, url, title) if id.trim forall (_.isDigit) =>
        WikiDocumentHeader(id.trim.toInt, title, url)
      case _ =>
        NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  private def parseHeader(attardiLines: IndexedSeq[String]): WikiDocument =
    attardiLines.find(_.contains(StartDocMark)) match {
      case Some(headerLine) => parseHeader(headerLine)
      case _ => NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  private def getBodyLines(attardiLines: IndexedSeq[String]): Option[IndexedSeq[String]] = {
    val bodyStart = attardiLines.indexWhere(_.contains(StartDocMark))
    val bodyEnd = attardiLines.lastIndexWhere(_.contains(EndDocMark))
    if (bodyStart >= 0 && bodyEnd - bodyStart > 1)
      Some(attardiLines.slice(bodyStart + 1, bodyEnd))
    else
      None
  }

  def apply(attardiLines: IndexedSeq[String], justHeader: Boolean = true): WikiDocument =
    parseHeader(attardiLines) match {
      case res@WikiDocumentHeader(id, title, url) =>
        if (justHeader)
          res
        else {
          getBodyLines(attardiLines) match {
            case bodyLines: Some[IndexedSeq[String]] =>
              new WikiDocumentWithBasicStats(id, title, url, bodyLines.value)
            case _ =>
              NoWikiDocument(ParseFailReason.BodyParsingFail)
          }
        }

      case fail => fail
    }
}
