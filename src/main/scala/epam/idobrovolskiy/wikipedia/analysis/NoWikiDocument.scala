package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
final case class NoWikiDocument(failReason: ParseFailReason.Value) extends WikiDocument {
  override def toString = s"PARSING FAILED (reason=$failReason)"
}
