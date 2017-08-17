package epam.idobrovolskiy.wikipedia.trending.time

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation

/**
  * Created by Igor_Dobrovolskiy on 15.08.2017.
  */
trait DateRangesExtractor {
  def extractRanges(s: String): Set[WikiDateRange]
  def extractRangesWithCitations(id: Int, s: String): Set[(WikiDateRange, WikiCitation)]
}
