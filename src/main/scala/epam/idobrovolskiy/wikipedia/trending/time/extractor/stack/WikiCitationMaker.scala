package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation

import scala.util.matching.Regex

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
trait WikiCitationMaker {
  def makeCitation(id: Int, s: String, m: Regex.Match): WikiCitation = {
    val startInd = m.start(0)
    val endInd = m.end(0)

    WikiCitation(id, startInd, endInd,
      s.substring( //TODO: extract citation taking into account sentence delimiters and whole words
        math.max(startInd - 20, 0),
        math.min(endInd + 20, s.length)
      )
    )
  }
}
