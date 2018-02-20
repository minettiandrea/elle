package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import services.TDLService

import scala.concurrent.ExecutionContext

@Singleton
class PagesController @Inject()(cc: ControllerComponents,tdlService:TDLService)(implicit ec: ExecutionContext) extends AbstractController(cc) {
  def home = Action.async { implicit r =>
    for{
      homeText <- tdlService.pagina("Homepage")
    } yield {
      Ok(views.html.home(homeText))
    }
  }
  def calendario = Action.async{ implicit r =>
    for{
    eventi <- tdlService.eventi(new java.util.Date().getTime,new java.util.Date().getTime + 10000000000000000L)
    } yield {
      Ok(views.html.eventi(eventi))
    }
  }
  def spazi = Action {
    Ok(views.html.spazi())
  }


  def contatti = Action {
    Ok(views.html.contatti())
  }

  def tdl = Action{
    Redirect(routes.PagesController.calendario(),PERMANENT_REDIRECT)
  }
  def chisiamo = Action.async { implicit r =>
    for{
      text <- tdlService.pagina("Chi siamo")
    } yield {
      Ok(views.html.chisiamo(text))
    }
  }
}
