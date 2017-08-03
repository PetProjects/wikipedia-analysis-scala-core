package epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi

import epam.idobrovolskiy.wikipedia.trending.document._
import epam.idobrovolskiy.wikipedia.trending.{DefaultTokenizer, TopTokenCount}

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
object AttardiWikiDocumentParser {

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

  def parseHeader(attardiLines: IndexedSeq[String]): WikiDocument =
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

  private def parseBasicBodyStats(bodyLines: IndexedSeq[String]) =
    new BasicBodyStats(bodyLines.length,
      DefaultTokenizer.getTopNTokens(bodyLines, TopTokenCount))

  def parseBasicStats(attardiLines: IndexedSeq[String]): WikiDocument =
    parseInternal(attardiLines, false)

  def parseFullText(attardiLines: IndexedSeq[String]): WikiDocument =
    parseInternal(attardiLines, true)

  private def createDocument(id: Int, title: String, url: String, bodyLines: IndexedSeq[String], fullText: Boolean): WikiDocument = {
    val stats = parseBasicBodyStats(bodyLines)
    if (fullText)
      new WikiDocumentFullText(id, title, url, stats, bodyLines)
    else
      new WikiDocumentWithBasicBodyStats(id, title, url, stats)
  }

  private def parseInternal(attardiLines: IndexedSeq[String], fullText: Boolean): WikiDocument =
    parseHeader(attardiLines) match {
      case WikiDocumentHeader(id, title, url) =>
        getBodyLines(attardiLines) match {
            case Some(bodyLines) =>
              createDocument(id, title, url, bodyLines, fullText)
            case _ =>
              NoWikiDocument(ParseFailReason.BodyParsingFail)
          }

      case fail => fail
    }
}
