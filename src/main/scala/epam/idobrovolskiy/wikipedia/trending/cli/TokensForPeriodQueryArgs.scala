package epam.idobrovolskiy.wikipedia.trending.cli

import epam.idobrovolskiy.wikipedia.trending.time.WikiDate

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
case class TokensForPeriodQueryArgs
(
  since: WikiDate,
  until: WikiDate,
  topN: Int,
  debug: Boolean,
  queryVersion: Int,
  useHive: Boolean
)
  extends WikiQueryArgs {
  override val queryType = WikiQueryType.TopTokensForPeriod

  override def toString: String = super.toString + s", since=$since, until=$until, topN=$topN"

  override def equals(obj: scala.Any): Boolean = obj match {
    case that: TokensForPeriodQueryArgs => super.equals(that) &&
      that.since == this.since &&
      that.until == this.until &&
      that.topN == this.topN
  }

  override def hashCode(): Int = (super.hashCode, since, until, topN).##

  override def canEqual(other: Any): Boolean = other.isInstanceOf[TokensForPeriodQueryArgs]
}

object TokensForPeriodQueryArgs {
  def apply(args: WikiQueryArgs)(d: Boolean = args.debug,
                                 qv: Int = args.queryVersion,
                                 uh: Boolean = args.useHive,
                                 tn: Int = 10,
                                 since: WikiDate = WikiDate.MinDate,
                                 until: WikiDate = WikiDate.Now): WikiQueryArgs =
    TokensForPeriodQueryArgs(since, until, topN = tn, debug = d, queryVersion = qv, useHive = uh)
}