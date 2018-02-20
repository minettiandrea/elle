package controllers

import java.time.Period
import java.time.temporal.TemporalUnit
import javax.inject._

import ch.wavein.mailchimp.MC
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import services.TDLService



import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class NewsletterController @Inject()(
                                      cc: ControllerComponents,
                                      tdlService: TDLService,
                                      configuration: Configuration
                                    )(implicit ec: ExecutionContext) extends AbstractController(cc) {


  def mc = MC(
    configuration.get[String]("mailchimp.key"),
    configuration.get[String]("mailchimp.list"),
    configuration.get[String]("mailchimp.zone")
  )

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def export() = Action.async { implicit request: Request[AnyContent] =>
    mc.members().map { members =>
      Ok(Json.toJson(members))
    }
  }

  def preview() = Action.async {  implicit request: Request[AnyContent] =>

    val start = 1517443200000L // new java.util.Date().getTime
    val end = 1519862400000L //new java.util.Date().toInstant.plus(Period.ofDays(35)).toEpochMilli

    for{
      eventi <- tdlService.eventi(start,end,true)
      esposizioni <- tdlService.esposizioni(start,end)
      newsletterForm <- tdlService.newsletter(start,end)
    } yield {
      Ok(views.html.newsletter.newsletter(eventi,newsletterForm,esposizioni))
    }
  }
}
