package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, ControllerComponents}

/**
  * Created by Igor_Dobrovolskiy on 28.08.2017.
  */

class GuiController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def gui = Action { implicit request =>
    Ok(views.html.gui())
  }
}