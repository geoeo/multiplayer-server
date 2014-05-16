package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import Utils.GameMapper

object Application extends Controller {

  val testPos = Json.toJson(
    Map(
      "x" -> Json.toJson(5),
      "y" -> Json.toJson(5),
      "angle" -> Json.toJson(50),
      "isJumping" -> Json.toJson(false)
    )
  )

  /** Message Constants */

  val waitResponse = "Waiting.."
  val initialResponse = "Player Joined"
  val readyResponse = "Ready"
  val disconnectResponse = "Disconnected"

  /** Defined: Utils.GameMapper.object */
  val gameMapper = GameMapper


  /** Routes */

  def index = Action ( Ok(views.html.index("Your new application is ready.")) )

  //TODO test gameMapping
  def lobbySocket : WebSocket[String] = WebSocket.using[String]( request =>
    (
      Iteratee.foreach[String]( _ =>
      {
        gameMapper.insertRequestIntoGameMapping(request)
        println(initialResponse)
      }).map( _ =>
      {
        gameMapper.removeRequestFromGameMapping(request)
        println(disconnectResponse)
      }),
      Enumerator(waitResponse+ "\nGame Ready? : " + gameMapper.isGameReadyWith(request))
    )
  )

  def testSocket : WebSocket[String] = WebSocket.using[String] {request =>

    val (in,out) =
      (
        Iteratee.foreach[String](println).map(_ => println(disconnectResponse)),
        Enumerator(testPos.toString())
      )

    (in,out)

  }

}