package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 29/05/2014
 * Time: 22:46
 */

// TODO synchronize generator of new game_id; it has to be unique

object GameIsReady {

//  val generator = new Random()

  var game_id = 0

  val value = Json.toJson(

    Map(
      "header" -> Json.toJson("status"),
      "body" -> Json.toJson(true),
      "game_id" -> Json.toJson(game_id)
    )

  )

  def generateValue  = {game_id += 1; value}

}
