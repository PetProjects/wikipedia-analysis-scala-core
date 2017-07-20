package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
object AttardiWikiDocument {

  //<doc id="12" url="https://en.wikipedia.org/wiki?curid=12" title="Anarchism">
  //<doc id="307" url="https://en.wikipedia.org/wiki?curid=307" title="Abraham Lincoln">
  private val FirstLineRE =
  """<doc id="(\d+)" url="([\S\d\.\?\=\#]+)" title="([^\"]+)">""".r
  private val StartDocMark = "<doc "
  private val EndDocMark = "</doc>"

  private def parseHeader(header: String): WikiDocument =
    header match {
      case FirstLineRE(id, url, title) if id.trim forall (_.isDigit) =>
        PlainWikiDocument(id.trim.toInt, title, url)
      case _ =>
        NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  private def parseHeader(attardiLines: List[String]): WikiDocument =
    attardiLines.find(_.contains(StartDocMark)) match {
      case Some(headerLine) => parseHeader(headerLine)
      case _ => NoWikiDocument(ParseFailReason.HeaderParsingFail)
    }

  private def parseBody(attardiLines: List[String]): scala.List[_root_.scala.Predef.String] =
    attardiLines.dropWhile (s => s.trim.isEmpty || s.contains("<doc"))
      .takeWhile (!_.contains(EndDocMark))

  def apply(attardiLines: List[String], toFull: Boolean = false): WikiDocument =
    parseHeader(attardiLines) match {
      case res@PlainWikiDocument(id, title, url) =>
        if (toFull)
          new FullWikiDocument(id, title, url, parseBody(attardiLines))
        else
          res

      case fail => fail
    }
}
