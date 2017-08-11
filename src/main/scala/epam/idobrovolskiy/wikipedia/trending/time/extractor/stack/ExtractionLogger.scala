package epam.idobrovolskiy.wikipedia.trending.time.extractor.stack

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

import scala.util.matching.Regex

/**
  * Created by Igor_Dobrovolskiy on 10.08.2017.
  */
trait ExtractionLogger extends BasicStackedDatesExtractor {
  def logExtractionIssue(issueDescription: String): Unit

  def tryExtract(dateStartInd: Int, m: Regex.Match)(pDate: => WikiDate): WikiDateExtraction =
    try {
      WikiDateExtraction(pDate, dateStartInd, m)
    }
    catch {
      case ex: Exception =>
        logExtractionIssue(s"Failed extracting date for '${m.matched}'\n$ex")
        WikiDateExtraction(WikiDate.NoDate, dateStartInd, m)
    }
}
