package model

import tdl.model.FormData

case class PaginaStaticaForm(
                           titolo:String,
                           testo:Option[String]
                         ) {
}

object PaginaStaticaForm{
  import io.circe._
  import io.circe.generic.auto._

  def formTDL(fds:Seq[FormData]):Seq[PaginaStaticaForm] = {
    for(fd <- fds) yield {
      fd.data.as[PaginaStaticaForm].right.toOption
    }
  }.flatten.map{ x =>
    x.copy(testo = x.testo.map(_.replaceAll("\n","<br>")))
  }
}
