package v1.wikiq

import javax.inject.Inject

import epam.idobrovolskiy.wikipedia.trending.cli.WikiDateRangeParser
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class WikiqRouter @Inject()(controller: WikiqController) extends SimpleRouter {
  val prefix = "/v1/wikiq"

  override def routes: Routes = {

    case GET(p"/tokens/$date_range") =>
      controller.tokens(date_range)

    case GET(p"/articles/$date_range") => {
      WikiDateRangeParser.parse(date_range) match {
        case Some(range) => println(range)
        case _ => throw new IllegalArgumentException(date_range)
      }

      ???
    }

    case GET(p"/distribution/$tokens") => {
      println(tokens)
      ???
    }
  }

}
