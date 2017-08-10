package epam.idobrovolskiy.wikipedia.trending.document

import scala.util.matching.Regex

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

object WikiCitation {
//  def apply(id: Int, bodyStartOffset: Int, bodyEndOffset: Int, citation: String) =
//    new WikiCitation(id, bodyStartOffset, bodyEndOffset, citation)

  def apply(id: Int, wholeText: String, mtch: Regex.Match): WikiCitation = {
    val startInd = mtch.start(0)
    val endInd = mtch.end(0)

    apply(id, startInd, endInd,
      wholeText.substring( //TODO: extract citation taking into account sentence delimiters and whole words
        math.max(startInd - 20, 0),
        math.min(endInd + 20, wholeText.length)
      )
    )
  }
}
