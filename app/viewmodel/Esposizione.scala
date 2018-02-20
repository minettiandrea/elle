package viewmodel



import java.text.SimpleDateFormat
import java.util.Locale

import model.EventoForm
import tdl.model.FormData

object Esposizione{

  def fromTDL(events:Seq[(FormData,EventoForm)]):Seq[Evento] = {
    events.map(fromFormData).groupBy(_.titolo).map{ case (titolo,events) =>
      val sortedEvents = events.sortBy(_.start)
      sortedEvents.head.copy(data = sortedEvents.map(_.data).mkString("<br>"), giorno = sortedEvents.map(_.giorno).mkString("/"))
    }
  }.toSeq

  private def fromFormData(event:(FormData,EventoForm)) = {

    val inizio = event._1.start.getOrElse(0L) - 60*60*1000
    val fine = event._1.end.getOrElse(0L) - 60*60*1000

    val format = new SimpleDateFormat("EEE, d MMMM",Locale.ITALIAN)
    val data = format.format(new java.util.Date(inizio)) + " - " + format.format(new java.util.Date(fine))

    val dayFormat = new SimpleDateFormat("d",Locale.ITALIAN)
    val day = dayFormat.format(new java.util.Date(inizio))

    val monthFormat = new SimpleDateFormat("MMMM",Locale.ITALIAN)
    val month = monthFormat.format(new java.util.Date(inizio)).toUpperCase()

    Evento(
      titolo = event._2.title,
      data = data,
      giorno = day,
      mese = month,
      descrizione = event._2.description.map(_.replaceAll("\n","<br>")),
      organizzatore = event._2.nomeorganizzatore,
      info = event._2.location,
      start = inizio,
      luogo = event._1.teamId match {
        case Some("592bf008630000630070891a") => "1° piano, Sala Grande"
        case Some("594926ad630000d50070891f") => "1° piano, Sala Piccola"
        case Some("594926b1630000c300708920") => "1° piano, Sala Incontro"
        case Some("5970bb19630000e907708961") => "piano -1, Atelier Acqua"
        case Some("5970bb37630000f407708962") => "piano -1, Atelier Polvere"
        case Some("5970bb46630000e007708963") => "3° piano, Camera oscura"
        case Some("5970bb616300000308708964") => "3° piano, Medialab"
        case Some("5970bb706300000708708965") => "3° piano, Residenza est"
        case Some("5970bb80630000ff07708966") => "3° piano, Residenza ovest"
        case Some("5970bb8a630000ff07708967") => "1° piano, Cucina"
        case Some("5a0c4ce32a0100a205be13d9") => "piano terra, Cambusa Teatro"
        case _ => ""
      }
    )
  }
}