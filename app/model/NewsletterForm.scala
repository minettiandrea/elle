package model

import tdl.model.FormData


case class NewsletterForm(
                       title:String,
                       intro:Option[String],
                       `date-index-field`:Long
                     ) {
}

object NewsletterForm{
  import io.circe._
  import io.circe.generic.auto._

  def formTDL(fds:Seq[FormData]):Option[NewsletterForm] = {
    for(fd <- fds) yield {
      fd.data.as[NewsletterForm].right.toOption
    }
  }.flatten.sortBy(-_.`date-index-field`).headOption.map{ x =>
    x.copy(intro = x.intro.map(_.replaceAll("\n","<br>")))
  }
}
