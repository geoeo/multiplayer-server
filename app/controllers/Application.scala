package controllers

import play.api.mvc._
import play.api.libs.iteratee.{Concurrent, Iteratee}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.{JsValue, Json}
import Utils.GameMapper
import Utils.StateMapper
import play.Logger

object Application extends Controller {

//  val gameIsReady : JsValue = model.GameIsReady.value
  val gameIsNotReady : JsValue = model.GameIsNotReady.value
  val testPos = model.testValuesForAPlayer.value

  /** Message Constants */

  val waitResponse = "Waiting.."
  val initialResponse = "Player Joined: "
  val readyResponse = "Ready"
  val disconnectResponse = "Disconnected: "
  val receivedResponse = "Received From: "

  /** Defined: Utils.GameMapper.object */
  val gameMapper = GameMapper

  /** Provides a simple lookup of request.id -> true/false */
  var registry : Map[Long,Boolean] = Map()

  /** Routes */

  def index = Action ( Ok(views.html.index("Your new application is ready.")) )

  /** add a new field in the session cookie
    * so that the client can be tracked across multiple requests
    * */

  def setUpSession = Action (implicit request => Ok("Set up").withSession(request.session + ("id" , request.id.toString) ))

  //TODO request id changes on socket change! fix persistence error

  def lobbySocket : WebSocket[String] = WebSocket.using[String]( request =>
    {
      /** Although it is possible to broadcast, in this scenario there is a 1 to 1 mapping
        * between channel and request */
      val (out,channel) = Concurrent.broadcast[String]

      (
        Iteratee.foreach[String]( _ =>
        {
          if(!registry.getOrElse(request.id,false)){

            /** request has been registered to the server */
            registry = registry.updated(request.id,true)

            gameMapper.insertRequestIntoGameMapping(request)

            if(Logger.isInfoEnabled)
              Logger.info(initialResponse + request.id )
          }

          if(gameMapper.isGameReadyWith(request)){
            channel.push(model.GameIsReady.generateValue(request.id).toString())}
          else
            channel.push(gameIsNotReady.toString())

        }).map( _ =>
        {
          registry = registry.updated(request.id,false)
          gameMapper.removeRequestFromGameMapping(request)

          if(Logger.isInfoEnabled)
            Logger.info(disconnectResponse + request.id)
        }),
        out
      )
    }
  )


  // TODO check data arrives at destination
  def dataSocket : WebSocket[String] = WebSocket.using[String] {request =>

    val (out,channel) = Concurrent.broadcast[String]

    (Iteratee.foreach[String]{ data =>

      val parsedData : JsValue = Json.parse(data)
      val game_id : Int = (parsedData \ "game_id").asOpt[Int].getOrElse(-1)

      if(Logger.isInfoEnabled){
        Logger.info(receivedResponse + request.id + " - " + data)
        Logger.info("game_id is: " + game_id)
      }

      /** update own state */
      StateMapper.updateState(game_id,parsedData)

      val opponent : RequestHeader = GameMapper.getOpponentOf(game_id)
      /** Lookup opponent state data and send back */
      channel.push(Json.stringify(StateMapper.lookUp(opponent.id.toInt)))

      }.map(_ => println(disconnectResponse)),
      out
    )


  }


}