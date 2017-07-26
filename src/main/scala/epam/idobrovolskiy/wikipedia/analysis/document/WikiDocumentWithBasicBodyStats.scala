package epam.idobrovolskiy.wikipedia.analysis.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
class WikiDocumentWithBasicBodyStats
(
  id: Int,
  title: String,
  url: String,
  val body: BasicBodyStats
)
  extends WikiDocumentWithBody(id, title, url)
{
  type TBody = BasicBodyStats
}
