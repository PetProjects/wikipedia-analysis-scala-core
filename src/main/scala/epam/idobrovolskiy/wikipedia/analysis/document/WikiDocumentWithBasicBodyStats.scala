package epam.idobrovolskiy.wikipedia.analysis.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
class WikiDocumentWithBasicBodyStats
    //TODO: Think over reworking to trait,
    // to make hierarchy clearer (it's too heavy atm considering WikiDocumentFullText at the bottom)
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
