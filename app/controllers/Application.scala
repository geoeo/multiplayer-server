package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def testSocket : WebSocket[String] = WebSocket.using[String] {request =>

    val in = Iteratee.foreach[String](println).map( _ => println("Disconnected") )

    val testPos = Json.toJson(
      Map(
        "x" -> Json.toJson(5),
        "y" -> Json.toJson(5),
        "angle" -> Json.toJson(50)
      )
    )

    val out = Enumerator(testPos.toString())

    (in,out)

  }

}