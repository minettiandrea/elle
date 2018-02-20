package model

import tdl.model.FormData

case class EventoForm(
                     title:String,
                     stato:String,
                     description:Option[String],
                     nomeorganizzatore:Option[String],
                     location:Option[String],
                     referentecomitato:Option[String]
) {
  def pubblicato = stato == "mmf2qbzmn"
}

object EventoForm{
  import io.circe._
  import io.circe.generic.auto._

  def formTDL(fds:Seq[FormData]):Seq[(FormData,EventoForm)] = {
    for(fd <- fds) yield {
      fd.data.as[EventoForm].right.toOption.map((fd,_))
    }
  }.flatten
}


