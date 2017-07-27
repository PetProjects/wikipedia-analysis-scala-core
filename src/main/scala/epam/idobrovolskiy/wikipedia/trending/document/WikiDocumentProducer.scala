package epam.idobrovolskiy.wikipedia.trending.document

/**
  * Created by igor.dobrovolskiy on 29.06.2017.
  */

trait WikiDocumentProducer {
  def getDocuments(path: String): Stream[WikiDocument]
}