package services

import model.{NewsletterForm, PaginaStaticaForm}
import viewmodel.Evento

import scala.concurrent.Future

trait TDLService {
  def eventi(start:Long,end:Long, grouped:Boolean = false):Future[Seq[Evento]]
  def esposizioni(start:Long,end:Long):Future[Seq[Evento]]
  def newsletter(start:Long,end:Long):Future[Option[NewsletterForm]]
  def pagina(titolo:String):Future[Option[PaginaStaticaForm]]
}
