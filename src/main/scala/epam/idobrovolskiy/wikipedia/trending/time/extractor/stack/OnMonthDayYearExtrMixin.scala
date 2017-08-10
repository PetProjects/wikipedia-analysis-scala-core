package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 09.08.2017.
  */
trait OnMonthDayYearExtrMixin extends InMonthYearExtrMixin with ExceptionLoggerExtrMixin {

  private val re = ("""(?:[^\w]|^)on\s""" + reMonths + """\s([1-3]?\d)(?:,|(?:\sof))?\s(\d{3,4})(?:[\s\.,]|$)""").r
  private val yearGroup = 3
  private val monthGroup = 1
  private val dayGroup = 2

  def extractInMonthDayYearDates(id: Int, s: String): Iterator[WikiDateExtraction] =
    for {cMatch <- (re findAllIn s).matchData} yield
    //      WikiDateExtraction(
    //        WikiDate.AD(
    //          cMatch.group(yearGroup).toInt,
    //          TextToMonths(cMatch.group(monthGroup)),
    //          cMatch.group(dayGroup).toInt),
    //        cMatch.start(yearGroup),
    //        cMatch)
      tryExtract(cMatch.start(yearGroup), cMatch) {
        WikiDate.AD(
          cMatch.group(yearGroup).toInt,
          TextToMonths(cMatch.group(monthGroup)),
          cMatch.group(dayGroup).toInt)
      }

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[WikiDateExtraction]) =
    super.appendDates(id, s, it ++ extractInMonthDayYearDates(id, s))
}