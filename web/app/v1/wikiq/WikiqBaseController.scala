package v1.wikiq

import javax.inject.Inject

import play.api.mvc.{BaseController, ControllerComponents}

/**
 * Exposes actions and handler to the PostController by wiring the injected state into the base class.
 */
class WikiqBaseController @Inject()(pcc: WikiqControllerComponents) extends BaseController with RequestMarkerContext {
  override protected def controllerComponents: ControllerComponents = pcc

  def PostAction: WikiqActionBuilder = pcc.postActionBuilder

  def postResourceHandler: WikiqResourceHandler = pcc.postResourceHandler
}
