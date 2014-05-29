package model

import play.api.libs.json.Json

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 27/05/2014
 * Time: 13:45
 */
object testValuesForAPlayer {

  val value = Json.toJson(
    Map(
      "header" -> Json.toJson("player"),
      "body" -> Json.toJson(
        Map(
          "x" -> Json.toJson(5),
          "y" -> Json.toJson(5),
          "angle" -> Json.toJson(50),
          "isJumping" -> Json.toJson(true),
          "shouldDie" -> Json.toJson(false)
        )
      )
    )
  )

}
