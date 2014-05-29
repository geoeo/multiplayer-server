package controllers

import play.api.mvc._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import scala.concurrent.ExecutionContext.Implicits.global
import Utils.GameMapper

object Application extends Controller {

  val gameIsReady = model.gameIsReady.value
  val testPos = model.testValuesForAPlayer.value

  /** Message Constants */

  val waitResponse = "Waiting.."
  val initialResponse = "Player Joined: "
  val readyResponse = "Ready"
  val disconnectResponse = "Disconnected: "
  val receivedResponse = "Received: "

  /** Defined: Utils.GameMapper.object */
  val gameMapper = GameMapper

  /** Provides a simple lookup of request.id -> true/false */
  var registry : Map[Long,Boolean] = Map()


  /** Routes */

  def index = Action ( Ok(views.html.index("Your new application is ready.")) )

  def lobbySocket : WebSocket[String] = WebSocket.using[String]( request =>
    (
      Iteratee.foreach[String]( _ =>
      {
        if(!registry.getOrElse(request.id,false)){
          registry = registry.updated(request.id,true)
          gameMapper.insertRequestIntoGameMapping(request)
          println(initialResponse + request.id )
        }

      }).map( _ =>
      {
        registry = registry.updated(request.id,false)
        gameMapper.removeRequestFromGameMapping(request)
        println(disconnectResponse + request.id)
      }),
      Enumerator(gameIsReady.toString)

    )
  )

  //TODO implement lookup and marshalling of player data to opponent
  def dataSocket : WebSocket[String] = WebSocket.using[String] {request =>

    val (in,out) =
      (
        Iteratee.foreach[String](data => println(receivedResponse + data)).map(_ => println(disconnectResponse)),
        Enumerator(gameIsReady.toString)
      )

    (in,out)

  }

}