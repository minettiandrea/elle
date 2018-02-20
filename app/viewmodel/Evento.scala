package viewmodel

import java.text.SimpleDateFormat
import java.util.Locale

import model.EventoForm
import tdl.model.FormData

case class Evento(
                 titolo:String,
                 data:String,
                 dataFull:String,
                 giorno:String,
                 mese:String,
                 descrizione:Option[String],
                 organizzatore:Option[String],
                 info:Option[String],
                 luogo:String,
                 start:Long
                 )

object Evento{

  def fromTDL(events:Seq[(FormData,EventoForm)],grouped:Boolean):Seq[Evento] = {
    val ev = events.map(fromFormData)

    if(grouped) {
      ev.groupBy(_.titolo).map { case (titolo, events) =>
        val sortedEvents = events.sortBy(_.start)
        sortedEvents.head.copy(data = sortedEvents.map(_.data).mkString("<br>"), giorno = sortedEvents.map(_.giorno).mkString("/"))
      }
    } else ev

  }.toSeq

  private def fromFormData(event:(FormData,EventoForm)) = {

    val date = event._1.start.getOrElse(0L) - 60*60*1000

    val format = new SimpleDateFormat("EEE, d ' - ore:' H:mm",Locale.ITALIAN)
    val data = format.format(new java.util.Date(date))

    val formatFull = new SimpleDateFormat("EEE, d MMMM ' - ore:' H:mm",Locale.ITALIAN)
    val dataFull = formatFull.format(new java.util.Date(date))

    val dayFormat = new SimpleDateFormat("d",Locale.ITALIAN)
    val day = dayFormat.format(new java.util.Date(date))

    val monthFormat = new SimpleDateFormat("MMMM",Locale.ITALIAN)
    val month = monthFormat.format(new java.util.Date(date)).toUpperCase()

    Evento(
      titolo = event._2.title,
      data = data,
      dataFull = dataFull,
      giorno = day,
      mese = month,
      descrizione = event._2.description.map(_.replaceAll("\n","<br>")),
      organizzatore = event._2.nomeorganizzatore,
      info = event._2.location,
      start = date,
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