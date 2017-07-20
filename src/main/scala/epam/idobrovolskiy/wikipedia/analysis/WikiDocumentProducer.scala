package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by igor.dobrovolskiy on 29.06.2017.
  */

trait WikiDocumentProducer {
  def getDocuments(path: String): Stream[WikiDocument]
}