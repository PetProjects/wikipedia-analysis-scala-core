package epam.idobrovolskiy.wikipedia.trending.time

/**
  * Created by Igor_Dobrovolskiy on 10.08.2017.
  */
import java.time.LocalDate

import epam.idobrovolskiy.wikipedia.trending.time.extractor.stack._

object StackedDatesExtractor extends BasicStackedDatesExtractor
//Any later mixin (trait) has higher priority,
// i.e. if both matched single date, later only would have effect
// on both extract() and extractWithCitations() results.
with InYearExtrMixin
with InAndYearExtrMixin
with OfYearExtrMixin
with OnMonthDayYearExtrMixin //includes "with InMonthYearExtractor" indirectly, as derived from
{
  override def extractionIssuesLogger(issueDescription: String): Unit = println(s"!!!!!! ${LocalDate.now()} " + issueDescription)
}