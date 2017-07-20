package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 20.07.2017.
  */
trait AttardiWikiDocumentParsingStrategy {
  def parse(lines: List[String]): WikiDocument
}

object AttardiWikiDocumentParsingStrategy {
  val ToPlain = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: List[String]): WikiDocument = AttardiWikiDocument(lines)
  }

  val ToFull = new AttardiWikiDocumentParsingStrategy {
    override def parse(lines: List[String]): WikiDocument = AttardiWikiDocument(lines, true)
  }
}
