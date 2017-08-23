package v1.wikiq

import javax.inject.Inject

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext

case class PostFormInput(title: String, body: String)

/**
  * Takes HTTP requests and produces JSON.
  */
class WikiqController @Inject()(cc: WikiqControllerComponents)(implicit ec: ExecutionContext)
  extends WikiqBaseController(cc) {

  private val logger = Logger(getClass)

  def tokens(date_range: String): Action[AnyContent] = PostAction.async { implicit request =>
    logger.trace("index: ")
    postResourceHandler.findTopTokens(date_range).map { posts =>
      Ok(Json.toJson(posts))
    }
  }
}
