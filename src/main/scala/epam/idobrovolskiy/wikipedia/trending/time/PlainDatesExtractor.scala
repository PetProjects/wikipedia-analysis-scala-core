package epam.idobrovolskiy.wikipedia.trending.time

import epam.idobrovolskiy.wikipedia.trending.document.WikiCitation

/**
  * Created by Igor_Dobrovolskiy on 31.07.2017.
  */
@deprecated("Use BasicStackedDatesExtractor instead. Configure it flexibly with adding proper set of mixin-s (epam.idobrovolskiy.wikipedia.trending.time.extractor.stack.*Extrator traits)", "Since 09.08.2017")
class PlainDatesExtractor extends DatesExtractor{
  override def extractDates(s: String): Set[WikiDate] = extractInternalAdHoc(s).collect {
    case (wikiDate, _, _) => wikiDate
  }

  override def extractDatesWithCitations(id: Int, s: String): Set[(WikiDate, WikiCitation)] =
    extractInternalAdHoc(s).collect {
      case (wikiDate, startInd, endInd) => (wikiDate,
        WikiCitation(id, startInd, endInd,
          s.substring( //TODO: extract citation taking into account sentence delimiters and whole words
            math.max(startInd - 20, 0),
            math.min(endInd + 20, s.length)
          )
        )
      )
    }

  val trSingleDtInPtrn = """[^\w][Ii]n\s{1,2}(\d{3,4})\s""".r
  val trSingleDtOfPtrn = """\sof\s{1,2}(\d{3,4})\s""".r

  val rePatterns = Set(trSingleDtInPtrn, trSingleDtOfPtrn)

  private def extractInternalAdHoc(s: String) : Set[(WikiDate, Int, Int)] = //TODO: much more sophisticated solution must be implemented later
    for{
      ptrn <- rePatterns
      cMatch <- (ptrn findAllIn s).matchData
    } yield (WikiDate.AD(cMatch.group(1).toInt), cMatch.start(0), cMatch.end(0))
}
