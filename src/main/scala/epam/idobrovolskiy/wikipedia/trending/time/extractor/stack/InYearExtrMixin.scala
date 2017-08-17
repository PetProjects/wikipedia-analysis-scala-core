package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
trait InYearExtrMixin extends BasicStackedDatesExtractor {

  private val re = """(?:[^\w]|^)in\s{1,2}(\d{3,4})(?:[\s\.,]|$)""".r //should "\?\!" be added as allowed ending?

  def extractInYearDates(id: Int, s: String): Iterator[DateExtraction] =
    for {cMatch <- (re findAllIn s).matchData} yield
      DateExtraction(
        WikiDate.AD(cMatch.group(1).toInt),
        cMatch.start(1),
        cMatch)

  def extractInYearRanges(id: Int, s: String): Iterator[RangeExtraction] =
    extractInYearDates(id, s).map(interpretDateAsRange(_))

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[DateExtraction]) =
    super.appendDates(id, s, it ++ extractInYearDates(id, s))

  abstract override protected def appendRanges(id: Int, s: String, it: Iterator[RangeExtraction]) =
    super.appendRanges(id, s, it ++ extractInYearRanges(id, s))
}
