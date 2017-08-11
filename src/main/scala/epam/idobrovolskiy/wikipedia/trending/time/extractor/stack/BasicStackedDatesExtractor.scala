package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation
import epam.idobrovolskiy.wikipedia.trending.time.{DatesExtractor, WikiDate}

import scala.util.matching.Regex

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
class BasicStackedDatesExtractor extends DatesExtractor {

  protected case class WikiDateExtraction
  (
    date: WikiDate,
    dateStartInd: Int,
    m: Regex.Match
  )

  final def extract(s: String): Set[WikiDate] =
    appendDates(-1 /*id doesn't matter here*/ , s.toLowerCase, Iterator.empty)
      .toList
      .groupBy(_.dateStartInd)
      .map { case (_, r) => r.head.date }
      .toSet

  final def extractWithCitations(id: Int, s: String): Set[(WikiDate, WikiCitation)] =
    appendDates(id, s.toLowerCase, Iterator.empty)
      .toList
      .groupBy(_.dateStartInd)
      .map { case (_, r) => (r.head.date, WikiCitation(id, s, r.head.m)) }
      .toSet

  protected def appendDates(id: Int, s: String, it: Iterator[WikiDateExtraction]) =
    it.filter(_.date != WikiDate.NoDate)
}
