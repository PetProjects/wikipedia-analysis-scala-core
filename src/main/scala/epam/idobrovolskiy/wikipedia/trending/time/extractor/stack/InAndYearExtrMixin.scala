package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 08.08.2017.
  */
trait InAndYearExtrMixin extends BasicStackedDatesExtractor {

  private val re = """(?:[^\w]|^)in\s+(\d{3,4})\s+and\s+(\d{3,4})(?:[\s\.,]|$)""".r

  def extractInAndYearDates(id: Int, s: String): Iterator[WikiDateExtraction] = (
      for {
        cMatch <- (re findAllIn s).matchData
      } yield Iterator(
        WikiDateExtraction(WikiDate.AD(cMatch.group(1).toInt), cMatch.start(1), cMatch),
        WikiDateExtraction(WikiDate.AD(cMatch.group(2).toInt), cMatch.start(2), cMatch)
      )
    ).flatMap(x => x)

  abstract override protected def appendDates(id: Int, s: String, it: Iterator[WikiDateExtraction]) =
    super.appendDates(id, s, it ++ extractInAndYearDates(id, s))
}
