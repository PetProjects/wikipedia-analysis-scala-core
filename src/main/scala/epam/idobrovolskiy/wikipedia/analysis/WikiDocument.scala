package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by igor.dobrovolskiy on 29.06.2017.
  */

sealed trait WikiDocument

case class PlainWikiDocument(id: Int, title: String, url: String) extends WikiDocument {
  override def toString = s"id=$id, title=$title, url=$url"
}

final class FullWikiDocument(id: Int, title: String, url: String, val body: String) extends PlainWikiDocument(id, title, url)

final case class NoWikiDocument(failReason: ParseFailReason.Value) extends WikiDocument {
  override def toString = s"PARSING FAILED (reason=$failReason)"
}
