package v1.wikiq

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

final case class WikiqData(token: String, count: Int)

class WikiqExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")


/**
  * A pure non-blocking interface for WikiqRepository
  */
trait WikiqRepository {
  def topTokens(dateRange: String)(implicit mc: MarkerContext): Future[Iterable[WikiqData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class WikiqRepositoryImpl @Inject()()(implicit ec: WikiqExecutionContext) extends WikiqRepository {

  private val logger = Logger(this.getClass)

  private val topTokensSample = List(
    WikiqData("roman", 107083),
    WikiqData("battle", 45988),
    WikiqData("war", 36189),
    WikiqData("caesar", 31264),
    WikiqData("rome", 30291),
    WikiqData("han", 28480),
    WikiqData("emperor", 27396),
    WikiqData("empire", 22657),
    WikiqData("century", 20370),
    WikiqData("dynasty", 20114)
  )

    override def topTokens(dateRange: String)(implicit mc: MarkerContext): Future[Iterable[WikiqData]] = {
      //    val optRes: Seq[String] = WikiDateRangeParser.parse(date_range) match {
      //      case Some(range) =>
      //        WikiHiveQueryApi.queryTokensForPeriod(spark,
      //          TokensForPeriodQueryArgs(range.since, range.until, 10))
      //
      //      case _ => throw new IllegalArgumentException(date_range)
      //    }

      Future {
        logger.trace(s"list: ")
        topTokensSample
      }
    }
}
