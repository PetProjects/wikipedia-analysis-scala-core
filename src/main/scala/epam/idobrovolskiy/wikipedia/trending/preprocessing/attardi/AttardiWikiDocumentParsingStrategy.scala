package epam.idobrovolskiy.wikipedia.trending.preprocessing.attardi

import epam.idobrovolskiy.wikipedia.trending.document.WikiDocument

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
trait AttardiWikiDocumentParsingStrategy {
  def parse(lines: IndexedSeq[String]): WikiDocument
}

object AttardiWikiDocumentParsingStrategy {
  val ToHeader = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: IndexedSeq[String]): WikiDocument =
      AttardiWikiDocumentParser.parseHeader(lines)
  }

  val ToBasicStats = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: IndexedSeq[String]): WikiDocument =
      AttardiWikiDocumentParser.parseBasicStats(lines)
  }

  val ToFullText = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: IndexedSeq[String]): WikiDocument =
      AttardiWikiDocumentParser.parseFullText(lines)
  }
}
