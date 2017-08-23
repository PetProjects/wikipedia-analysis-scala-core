package v1.wikiq

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying wiki query information.
  */
case class WikiqResource(token: String, count: Int)

object WikiqResource {

  /**
    * Mapping to write a WikiqResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[WikiqResource] {
    def writes(res: WikiqResource): JsValue = {
      Json.obj(
        "token" -> res.token,
        "count" -> res.count
      )
    }
  }
}

/**
  * Controls access to the backend data, returning [[WikiqResource]]
  */
class WikiqResourceHandler @Inject()(routerProvider: Provider[WikiqRouter], wikiqRepository: WikiqRepository)
                                    (implicit ec: ExecutionContext) {

  def findTopTokens(dateRange: String)(implicit mc: MarkerContext): Future[Iterable[WikiqResource]] = {
    wikiqRepository.topTokens(dateRange).map { postDataList =>
      postDataList.map(postData => createWikiqResource(postData))
    }
  }

  private def createWikiqResource(q: WikiqData): WikiqResource = {
    WikiqResource(q.token, q.count)
  }
}
