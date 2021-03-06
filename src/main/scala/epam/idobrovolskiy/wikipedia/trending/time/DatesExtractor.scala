package epam.idobrovolskiy.wikipedia.trending.time

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */
trait DatesExtractor {
  def extractDates(s: String): Set[WikiDate]
  def extractDatesWithCitations(id: Int, s: String): Set[(WikiDate, WikiCitation)]
}
