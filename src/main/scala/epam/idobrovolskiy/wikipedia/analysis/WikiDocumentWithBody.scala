package epam.idobrovolskiy.wikipedia.analysis

/**
  * Created by Igor_Dobrovolskiy on 21.07.2017.
  */
abstract class WikiDocumentWithBody
(
  id: Int,
  title: String,
  url: String,
  bodyLines: Seq[String]
)
  extends WikiDocumentHeader(id, title, url) {
  type TBody

  def parseBody(bodyLines: Seq[String]): TBody

  final val body: TBody = parseBody(bodyLines)

  override val toString = super.toString + s", $body"
}
