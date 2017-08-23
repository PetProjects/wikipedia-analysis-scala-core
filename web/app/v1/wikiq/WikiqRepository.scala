package v1.wikiq

import java.io.PrintWriter
import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import epam.idobrovolskiy.wikipedia.trending.cli.{TokensForPeriodQueryArgs, WikiDateRangeParser, WikiQueryArgs}
import epam.idobrovolskiy.wikipedia.trending.querying.WikiHiveQueryApi
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

  //  private def cmdLine(query: String) =
  //    s"beeline --hiveconf hive.tez.container.size=1024 -u jdbc:hive2://localhost:10000/wikitrending --showHeader=false --silent=true --verbose=false -n hive -p hive -e '${query.replace('\n', ' ')}'"
  private def cmdLine(fname: String) =
  s"beeline --hiveconf hive.tez.container.size=1024 -u jdbc:hive2://localhost:10000/wikitrending --showHeader=false --silent=true --verbose=false -n hive -p hive -f $fname"

  def parseBeelineOutput(beelineOutput: List[String]): Iterable[WikiqData] =
    beelineOutput.map(_.split("\\|").filter(_.length > 0).toList) collect {
      case token :: count :: _ => WikiqData(token.trim, count.trim.toInt)
    }

  def saveToRandTmpFile(hiveQuery: String): String = {
    val uuid = java.util.UUID.randomUUID.toString
    val fname = s"/tmp/$uuid"

    val writer = new PrintWriter(fname)
    try {
      writer.println(hiveQuery)
    } finally {
      writer.close()
    }

    fname
  }

  override def topTokens(dateRange: String)(implicit mc: MarkerContext): Future[Iterable[WikiqData]] = {
    Future {
      logger.trace(s"topTokens: $dateRange")

      WikiDateRangeParser.parse(dateRange) match {
        case Some(range) => {
          val args: TokensForPeriodQueryArgs = TokensForPeriodQueryArgs(WikiQueryArgs.DefaultArgs)(since = range.since, until = range.until)
          val hiveQuery = WikiHiveQueryApi.getHiveQueryFor(args)
          val fname = saveToRandTmpFile(hiveQuery)

          logger.trace(s" query fname: $fname")

          import scala.sys.process._

          val beelineOutput = cmdLine(fname).lineStream_!.toList

          logger.trace(s" beeline output: ${beelineOutput.mkString("\n")}")

          parseBeelineOutput(beelineOutput)
        }

        case _ => throw new IllegalArgumentException(dateRange)
      }
    }
  }
}
