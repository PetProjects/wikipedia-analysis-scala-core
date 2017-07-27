package epam.idobrovolskiy.wikipedia.trending.document

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
abstract class WikiDocumentWithBody
(
  id: Int,
  title: String,
  url: String
)
  extends WikiDocumentHeader(id, title, url)
{
  type TBody
  val body: TBody

  override val toString = super.toString + s", $body"
}
