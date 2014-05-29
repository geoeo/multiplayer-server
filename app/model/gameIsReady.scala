package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 29/05/2014
 * Time: 22:46
 */
object gameIsReady {

  val value = Json.toJson(

    Map(
      "header" -> Json.toJson("status"),
      "body" -> Json.toJson(true)
    )

  )

}
