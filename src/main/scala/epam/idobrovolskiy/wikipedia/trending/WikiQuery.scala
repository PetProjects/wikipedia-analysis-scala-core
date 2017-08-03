package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.cli.{TokensForPeriodQueryArgs, WikiQueryArgs, WikiQueryArgsParser}
import epam.idobrovolskiy.wikipedia.trending.querying.WikiQueryApi

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQuery extends App {
  WikiQueryArgsParser.parse(args) match {
    case Some(args: TokensForPeriodQueryArgs) => {
      if(args.debug) println(args)

      val queryRes = WikiQueryApi.queryTokensForPeriod(args)

      println(s"QUERY RESULTS:\n${queryRes.mkString("\n")}")
    }

    case Some(_: WikiQueryArgs) =>
      throw new UnsupportedOperationException("Any query types but --tokens are not supported atm.")

    case _ =>
  }

  spark.stop()
}
