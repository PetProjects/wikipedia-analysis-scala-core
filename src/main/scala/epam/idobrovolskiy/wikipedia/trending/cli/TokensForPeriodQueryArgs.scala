package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
case class TokensForPeriodQueryArgs
(
  since: WikiDate,
  until: WikiDate,
  topN: Int = 10,
  debug: Boolean = false,
  queryVersion: Int = -1,
  useHive: Boolean = false
)
  extends WikiQueryArgs
{
  override val queryType = WikiQueryType.TopTokensForPeriod

  override def toString: String = super.toString + s", since=$since, until=$until, topN=$topN"

  override def equals(obj: scala.Any): Boolean = super.equals(obj)

  override def canEqual(other: Any): Boolean = other.isInstanceOf[TokensForPeriodQueryArgs]
}
