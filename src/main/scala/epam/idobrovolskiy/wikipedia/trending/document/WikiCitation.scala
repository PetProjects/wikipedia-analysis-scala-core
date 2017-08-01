package epam.idobrovolskiy.wikipedia.trending.document

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */
case class WikiCitation
(
  id: Int,
  bodyStartOffset: Int,
  bodyEndOffset: Int,
  citation: String
)
  extends WikiDocument
{
  override def toString = s"\u201e...$citation...\u201c [$id; $bodyStartOffset..$bodyEndOffset]"
}