package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
final class WikiDocumentWithBasicStats
(
  id: Int,
  title: String,
  url: String,
  bodyLines: Seq[String]
)
  extends WikiDocumentWithBody(id, url, title, bodyLines)
{
  type TBody = BasicBodyStats

  def parseBody(bodyLines: Seq[String]) = new BasicBodyStats(bodyLines)
}
