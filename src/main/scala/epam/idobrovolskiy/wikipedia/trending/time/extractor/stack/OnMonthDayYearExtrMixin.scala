package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 09.08.2017.
  */
trait OnMonthDayYearExtrMixin extends InMonthYearExtrMixin with ExtractionLogger {

  private val re = ("""(?:[^\w]|^)on\s""" + reMonths + """\s([1-3]?\d)(?:,|(?:\sof))?\s(\d{3,4})(?:[\s\.,]|$)""").r
  private val yearGroup = 3
  private val monthGroup = 1
  private val dayGroup = 2

  def extractOnMonthDayYearDates(id: Int, s: String): Iterator[DateExtraction] =
    for {cMatch <- (re findAllIn s).matchData} yield
      tryExtract(cMatch.start(yearGroup), cMatch) {
        WikiDate.AD(
          cMatch.group(yearGroup).toInt,
          TextToMonths(cMatch.group(monthGroup)),
          cMatch.group(dayGroup).toInt)
      }

  def extractOnMonthDayYearRanges(id: Int, s: String): Iterator[RangeExtraction] =
    extractOnMonthDayYearDates(id, s).map(interpretDateAsRange(_))

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[DateExtraction]) =
    super.appendDates(id, s, it ++ extractOnMonthDayYearDates(id, s))

  abstract override protected def appendRanges(id: Int, s: String, it: Iterator[RangeExtraction]) =
    super.appendRanges(id, s, it ++ extractOnMonthDayYearRanges(id, s))
}