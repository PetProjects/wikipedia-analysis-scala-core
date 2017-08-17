package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 11.08.2017.
  */
trait YearEraExtrMixin extends BasicStackedDatesExtractor with BasicEraExtrMixin with ExtractionLogger {

  private val re = ("""(?:\s|\()(\d{0,2}(?:\,?\d)?\d\d)\s""" + //should 6th \d? be added at the beginning of year group? should 2nd digit in year be optional?
    reEra + """(?:[\s\.,\)]|$)""").r //should "\?\!" be added as allowed ending?

  private val yearGroup = 1
  private val eraGroup = 2

  def extractInYearEraDates(id: Int, s: String): Iterator[DateExtraction] = {
    for {
      cMatch <- (re findAllIn s).matchData
      if cMatch.group(eraGroup) != null
      optIsCommonEra = textToCommonEra(cMatch.group(eraGroup))
    //      if optIsCommonEra.isDefined //uncomment, if want to exclude logging extraction failures
    } yield tryExtract(cMatch.start(yearGroup), cMatch) {
      WikiDate.date(cMatch.group(yearGroup).filter(_ != ',').toInt, optIsCommonEra.get) //once era failed to be extracted, it will be logged with NoSuchElementException.
    }
  }

  def extractInYearEraRanges(id: Int, s: String): Iterator[RangeExtraction] =
    extractInYearEraDates(id, s).map(interpretDateAsRange(_))

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[DateExtraction]) =
    super.appendDates(id, s, it ++ extractInYearEraDates(id, s))

  abstract override protected def appendRanges(id: Int, s: String, it: Iterator[RangeExtraction]) =
    super.appendRanges(id, s, it ++ extractInYearEraRanges(id, s))
}
