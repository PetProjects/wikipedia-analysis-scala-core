package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
object AttardiWikiDocument {

  //<doc id="12" url="https://en.wikipedia.org/wiki?curid=12" title="Anarchism">
  //<doc id="307" url="https://en.wikipedia.org/wiki?curid=307" title="Abraham Lincoln">
  private val FirstLineRE = """<doc id="(\d+)" url="([\S\d\.\?\=\#]+)" title="([^\"]+)">""".r

  private def parseHeader(header: String): WikiDocument =
    header match {
      case FirstLineRE(id, url, title) => PlainWikiDocument(id.toInt, title, url) //TODO: process parsing Int in fail case
      case _ => NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  private def parseHeader(attardiLines: List[String]): WikiDocument =
    attardiLines.find(_.contains("<doc ")) match {
      case Some(headerLine) => parseHeader(headerLine)
      case _ => NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  def apply(attardiLines: List[String]): WikiDocument =
    parseHeader(attardiLines)
}
