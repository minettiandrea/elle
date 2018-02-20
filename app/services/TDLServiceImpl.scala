package services

import javax.inject.Inject

import model.{EventoForm, NewsletterForm, PaginaStaticaForm}
import play.api.{Configuration, Logger}
import tdl.TDL
import viewmodel.{Esposizione, Evento}

import scala.concurrent.ExecutionContext

class TDLServiceImpl @Inject()(configuration: Configuration)(implicit ec:ExecutionContext) extends TDLService {

  val formIdsEventi = Seq("arti_performative","arti_visive","letteratura_e_cinema","atelier","socio_ricreativo","musica","5279248d160000160096c184")
  val formIdsEsposizioni = Seq("arti_visive_esposizione")
  val teamIds = Seq("592bf008630000630070891a","594926ad630000d50070891f","594926b1630000c300708920","5970bb19630000e907708961","5970bb37630000f407708962","5970bb46630000e007708963","5970bb616300000308708964","5970bb706300000708708965","5970bb80630000ff07708966","5970bb8a630000ff07708967","5a0c4ce32a0100a205be13d9")


  def tdl = TDL(
    configuration.get[String]("tdl.username"),
    configuration.get[String]("tdl.key"),
    configuration.get[String]("tdl.endpoint")
  )

  override def eventi(start:Long,end:Long,grouped:Boolean = false) = for{
    teams <- tdl.teams()
    filteredTeam = teams.filter(_._id.exists(id => teamIds.contains(id)))
    forms <- tdl.forms()
    filteredFormsEventi = forms.filter(_._id.exists(id => formIdsEventi.contains(id)))
    eventiForm <- tdl.formData(start,end,filteredFormsEventi,filteredTeam)
  } yield {
    val eventiTDL = EventoForm.formTDL(eventiForm).filter(_._2.pubblicato)
    val eventi = Evento.fromTDL(eventiTDL,grouped).sortBy(_.start)
    eventi
  }

  override def esposizioni(start: Long, end: Long) = for{
    teams <- tdl.teams()
    filteredTeam = teams.filter(_._id.exists(id => teamIds.contains(id)))
    forms <- tdl.forms()
    filteredFormsEsposizioni = forms.filter(_._id.exists(id => formIdsEsposizioni.contains(id)))
    esposizioniForm <- tdl.formData(start,end,filteredFormsEsposizioni,filteredTeam)
  } yield {
    val esposizioniTDL = EventoForm.formTDL(esposizioniForm).filter(_._2.pubblicato)
    Esposizione.fromTDL(esposizioniTDL).sortBy(_.start)
  }

  override def newsletter(start: Long, end: Long) = for{
    teams <- tdl.teams()
    forms <- tdl.forms()
    parameterForm = forms.filter(_._id.contains("newsletter"))
    parameters <- tdl.formData(start,end,parameterForm,teams)
  } yield NewsletterForm.formTDL(parameters)

  override def pagina(titolo: String) = for{
    teams <- tdl.teams()
    forms <- tdl.forms()
    parameterForm = forms.filter(_._id.contains("5a8c077996000090041e4b95"))
    parameters <- tdl.formData(parameterForm)
  } yield PaginaStaticaForm.formTDL(parameters).find(_.titolo == titolo)
}
