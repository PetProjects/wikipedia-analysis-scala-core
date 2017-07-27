package epam.idobrovolskiy.wikipedia.trending.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
case class WikiDocumentHeader(id: Int, title: String, url: String)
  extends WikiDocument {
  override def toString = s"id=$id, title=$title, url=$url"
}
