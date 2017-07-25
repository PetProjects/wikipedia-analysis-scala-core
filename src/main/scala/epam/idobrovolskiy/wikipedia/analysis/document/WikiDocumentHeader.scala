package epam.idobrovolskiy.wikipedia.analysis.document

import epam.idobrovolskiy.wikipedia.analysis.WikiDocument

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
case class WikiDocumentHeader(id: Int, title: String, url: String)
  extends WikiDocument {
  override def toString = s"id=$id, title=$title, url=$url"
}
