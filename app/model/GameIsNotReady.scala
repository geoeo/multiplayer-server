package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 02/06/2014
 * Time: 12:49
 */
object GameIsNotReady {

  val value = Json.toJson(

    Map(
      "header" -> Json.toJson("status"),
      "body" -> Json.toJson(false)
    )

  )




}
