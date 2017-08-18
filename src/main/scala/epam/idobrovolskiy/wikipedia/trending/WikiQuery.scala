package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.cli.{TokensForPeriodQueryArgs, WikiQueryArgs, WikiQueryArgsParser}
import epam.idobrovolskiy.wikipedia.trending.querying.{WikiQueryApi, WikiHiveQueryApi}

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQuery extends App {
  WikiQueryArgsParser.parse(args) match {
    case Some(args: TokensForPeriodQueryArgs) if !args.useHive => {
      if(args.debug) println(args)

      val queryRes: Option[Seq[String]] = args.queryVersion match {
        case 1 => Some(WikiQueryApi.queryTokensForPeriodV1(args))
        case 2 => Some(WikiQueryApi.queryTokensForPeriodV2(args))

        case -1 /*latest*/ => Some(WikiQueryApi.queryTokensForPeriodV2(args))
        case _=> { println(s"Unsupported query version specified: ${args.queryVersion}") ; None }
      }

      if(queryRes.isDefined)
        println(s"QUERY RESULTS:\n${queryRes.get.mkString("\n")}")
    }

    case Some(args: TokensForPeriodQueryArgs) if args.useHive => {
      if(args.debug) println(args)

      val queryRes: Seq[String] = WikiHiveQueryApi.queryTokensForPeriod(spark, args)

      println(s"QUERY RESULTS:\n${queryRes.mkString("\n")}")
    }

    case Some(_: WikiQueryArgs) =>
      throw new UnsupportedOperationException("Any query types but --tokens are not supported atm.")

    case _ =>
  }

  spark.stop()
}
