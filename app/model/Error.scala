package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 01/06/2014
 * Time: 22:25
 */
object Error {

  val value = Json.toJson(Map( "header" -> Json.toJson("error"),"body" -> Json.toJson("no data")))


}
