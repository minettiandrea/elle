package model

import tdl.model.FormData


case class NewsletterForm(
                       title:String,
                       intro:Option[String]
                     ) {
}

object NewsletterForm{
  import io.circe._
  import io.circe.generic.auto._

  def formTDL(fds:Seq[FormData]):Option[NewsletterForm] = {
    for(fd <- fds) yield {
      fd.data.as[NewsletterForm].right.toOption
    }
  }.flatten.headOption.map{ x =>
    x.copy(intro = x.intro.map(_.replaceAll("\n","<br>")))
  }
}
