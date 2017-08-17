package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import java.io.Serializable
import java.time.{LocalDate, Month}

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation
import epam.idobrovolskiy.wikipedia.trending.time._

import scala.util.matching.Regex

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
class BasicStackedDatesExtractor extends DatesExtractor with DateRangesExtractor { //TODO: Is it a good idea to have BasicStackedEntityExtractor and derived classes BasicStackedDatesExtractor and BasicStackedRangesExtractor? How is it better to share logic among mixins then?

  protected trait DateEntityExtraction[Entity <: Serializable] /*think over better upper bounds for WikiDate and WikiDateRange*/ {
    val date: Entity
    val dateStartInd: Int
    val m: Regex.Match
  }

  protected case class DateExtraction
  (
    date: WikiDate,
    dateStartInd: Int,
    m: Regex.Match
  ) extends DateEntityExtraction[WikiDate]

  protected case class RangeExtraction
  (
    date: WikiDateRange,
    dateStartInd: Int,
    m: Regex.Match
  ) extends DateEntityExtraction[WikiDateRange]

  def extractDates(s: String): Set[WikiDate] =
    extractEntities[WikiDate, DateExtraction](s)(appendDates)

  //    appendDates(-1 /*id doesn't matter here*/ , s.toLowerCase, Iterator.empty)
  //      .toList
  //      .groupBy(_.dateStartInd)
  //      .map { case (_, r) => r.head.date }
  //      .toSet

  def extractDatesWithCitations(id: Int, s: String): Set[(WikiDate, WikiCitation)] =
    extractEntitiesWithCitations[WikiDate, DateExtraction](id, s)(appendDates)

  //    appendDates(id, s.toLowerCase, Iterator.empty)
  //      .toList
  //      .groupBy(_.dateStartInd)
  //      .map { case (_, r) => (r.head.date, WikiCitation(id, s, r.head.m)) }
  //      .toSet

  def extractRanges(s: String): Set[WikiDateRange] =
    extractEntities[WikiDateRange, RangeExtraction](s)(appendRanges)

  def extractRangesWithCitations(id: Int, s: String): Set[(WikiDateRange, WikiCitation)] =
    extractEntitiesWithCitations[WikiDateRange, RangeExtraction](id, s)(appendRanges)

  protected def appendDates(id: Int, s: String, it: Iterator[DateExtraction]) =
    it.filter(_.date != WikiDate.NoDate)

  protected def appendRanges(id: Int, s: String, it: Iterator[RangeExtraction]) =
    it.filter(_.date != WikiDateRange.NoDateRange)

  protected def interpretDateAsRange(extr: DateExtraction): RangeExtraction =
    RangeExtraction(interpretDateAsRange(extr.date), extr.dateStartInd, extr.m)

  protected def lastDayOfMonth(year: Int, month: Month, era: Boolean): Int =
    LocalDate.of(if (era) year else -year, month, 1).plusMonths(1).minusDays(1).getDayOfMonth

  protected def interpretDateAsRange(date: WikiDate): WikiDateRange =
    WikiDateRange(date, inferEndOfRangeFromDate(date))

  protected def inferEndOfRangeFromDate(date: WikiDate): WikiDate =
    date match { //TODO: refactor to use single code base here and at WikiDateRangeParser.inferUntilDateFromGaps
      case WikiDate(None, None, Some(year), None, None, None, Some(era)) =>
        WikiDate.date(year, Month.DECEMBER, 31, era)

      case WikiDate(None, Some(month), Some(year), None, None, None, Some(era)) =>
        WikiDate.date(year, month, lastDayOfMonth(year, month, era), era)

      case date => date
    }

  private def extractEntities[Entity <: Serializable, Extraction <: DateEntityExtraction[Entity]]
    (s: String)(appendEntities: (Int, String, Iterator[Extraction]) => Iterator[Extraction])
    : Set[Entity] = {

      appendEntities(-1 /*id doesn't matter here*/ , s.toLowerCase, Iterator.empty)
        .toList
        .groupBy(_.dateStartInd)
        .map { case (_, r) => r.head.date }
        .toSet
    }

  private def extractEntitiesWithCitations[Entity <: Serializable, Extraction <: DateEntityExtraction[Entity]]
    (id: Int, s: String)(appendEntities: (Int, String, Iterator[Extraction]) => Iterator[Extraction])
    : Set[(Entity, WikiCitation)] = {

      appendEntities(id, s.toLowerCase, Iterator.empty)
        .toList
        .groupBy(_.dateStartInd)
        .map { case (_, r) => (r.head.date, WikiCitation(id, s, r.head.m)) }
        .toSet
    }
}
