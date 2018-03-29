package controllers

import java.time.{LocalDate, Period, ZoneId}
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

  def preview(month:Int,year:Int) = Action.async {  implicit request: Request[AnyContent] =>

    val startDate = LocalDate.of(year,month,1) // new java.util.Date().getTime
    val endDate = startDate.plusMonths(1) //new java.util.Date().toInstant.plus(Period.ofDays(35)).toEpochMilli

    val start = startDate.atStartOfDay(ZoneId.of("Europe/Zurich")).toInstant.toEpochMilli
    val end = endDate.atStartOfDay(ZoneId.of("Europe/Zurich")).toInstant.toEpochMilli

    for{
      eventi <- tdlService.eventi(start,end,true)
      esposizioni <- tdlService.esposizioni(start,end)
      newsletterForm <- tdlService.newsletter(start,end)
    } yield {
      Ok(views.html.newsletter.newsletter(eventi,newsletterForm,esposizioni))
    }
  }
}
