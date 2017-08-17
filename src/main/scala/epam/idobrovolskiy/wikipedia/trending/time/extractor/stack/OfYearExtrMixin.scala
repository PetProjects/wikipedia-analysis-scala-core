package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
trait OfYearExtrMixin extends BasicStackedDatesExtractor {
  private val re = """\sof\s{1,2}(\d{3,4})(?:[\s\.,]|$)""".r

  def extractOfYearDates(id: Int, s: String): Iterator[DateExtraction] =
    for {cMatch <- (re findAllIn s).matchData} yield
      DateExtraction(
        WikiDate.AD(cMatch.group(1).toInt),
        cMatch.start(1),
        cMatch)

  def extractOfYearRanges(id: Int, s: String): Iterator[RangeExtraction] =
    extractOfYearDates(id, s).map(interpretDateAsRange(_))

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[DateExtraction]) =
    super.appendDates(id, s, it ++ extractOfYearDates(id, s))

  abstract override protected def appendRanges(id: Int, s: String, it: Iterator[RangeExtraction]) =
    super.appendRanges(id, s, it ++ extractOfYearRanges(id, s))
}
