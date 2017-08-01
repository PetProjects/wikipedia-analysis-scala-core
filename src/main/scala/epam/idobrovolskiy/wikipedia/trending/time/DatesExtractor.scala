package epam.idobrovolskiy.wikipedia.trending.time.date

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */
trait DatesExtractor {
  def extract(s: String): Seq[WikiDate]
  def extractWithCitations(id: Int, s: String): Seq[(WikiDate, WikiCitation)]
}
