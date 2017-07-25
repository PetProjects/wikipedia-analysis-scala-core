package epam.idobrovolskiy.wikipedia.analysis.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
class WikiDocumentWithBasicStats
(
  id: Int,
  title: String,
  url: String,
  basicStats: BasicBodyStats
)
  extends WikiDocumentWithBody(id, title, url)
{
  type TBody = BasicBodyStats

  val body: BasicBodyStats = basicStats
}
