package Utils

import play.api.libs.json.JsValue

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 01/06/2014
 * Time: 22:13
 */
object StateMapper {

  //TODO evaluate possible race conditions in StateMapper

  var requestStateMap : Map[Int,Option[JsValue]] = Map (1 -> None)

  def updateState(index : Int , data : JsValue) = requestStateMap = requestStateMap.updated(index,Some(data))

  def lookUp(index : Int) : JsValue = requestStateMap.getOrElse(index,None) match {
    case Some(data) => data
    case None => model.Error.value
  }


}
