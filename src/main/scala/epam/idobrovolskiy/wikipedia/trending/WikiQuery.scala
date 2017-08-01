package epam.idobrovolskiy.wikipedia.trending

import epam.idobrovolskiy.wikipedia.trending.cli.{TokensForPeriodQueryArgs, WikiQueryArgs, WikiQueryArgsParser}

/**
  * Created by Igor_Dobrovolskiy on 01.08.2017.
  */
object WikiQuery extends App {
  WikiQueryArgsParser.parse(args) match {
    case Some(args: TokensForPeriodQueryArgs) => ???
    case Some(_: WikiQueryArgs) => ???
    case _ =>
  }
}
