package epam.idobrovolskiy.wikipedia.analysis.attardi

import epam.idobrovolskiy.wikipedia.analysis._

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
trait AttardiWikiDocumentParsingStrategy {
  def parse(lines: IndexedSeq[String]): WikiDocument
}

object AttardiWikiDocumentParsingStrategy {
  val ToHeader = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: IndexedSeq[String]): WikiDocument = AttardiWikiDocument(lines)
  }

  val ToBasicStats = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: IndexedSeq[String]): WikiDocument = AttardiWikiDocument(lines, false)
  }
}
