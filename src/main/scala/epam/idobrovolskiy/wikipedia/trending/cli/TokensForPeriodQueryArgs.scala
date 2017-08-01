package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.time.date.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
case class TokensForPeriodQueryArgs
(
  since: WikiDate,
  until: WikiDate,
  topN: Int = 10
)
  extends WikiQueryArgs
{
  val queryType = WikiQueryType.TopTokensForPeriod
}
