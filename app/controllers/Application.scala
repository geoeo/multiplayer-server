package controllers

import play.api.mvc._
import play.api.libs.iteratee.{Enumerator, Iteratee}
import scala.concurrent.ExecutionContext.Implicits.global
import Utils.GameMapper

object Application extends Controller {

  val testPos = model.testValuesForAPlayer.value

  /** Message Constants */

  val waitResponse = "Waiting.."
  val initialResponse = "Player Joined: "
  val readyResponse = "Ready"
  val disconnectResponse = "Disconnected: "

  /** Defined: Utils.GameMapper.object */
  val gameMapper = GameMapper

  /** Provides a simple lookup of request.id -> true/false */
  var registry : Map[Long,Boolean] = Map()


  /** Routes */

  def index = Action ( Ok(views.html.index("Your new application is ready.")) )

  //TODO test gameMapping
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
      Enumerator(
        waitResponse+ "\nGame Ready? : " + gameMapper.isGameReadyWith(request)
        )
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