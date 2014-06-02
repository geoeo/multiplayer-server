package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 29/05/2014
 * Time: 22:46
 */

object GameIsReady {

  def generateValue(id : Long)
  = Json.toJson(Map("header" -> Json.toJson("status"),"body" -> Json.toJson(true),"game_id" -> Json.toJson(id)))

}
