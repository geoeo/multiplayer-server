package Utils

import play.api.mvc.{Headers, RequestHeader}

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 16/05/2014
 * Time: 10:41
 */
class RequestNotFound extends RequestHeader {

  /** flags invalid request */
  val id : Long = -1

  val uri : String = null
  val remoteAddress : String = null

  override def tags: Map[String, String] = null

  override def headers: Headers = null

  override def queryString: Map[String, Seq[String]] = null

  override def version: String = null

  override def method: String = null

  override def path: String = null
}
