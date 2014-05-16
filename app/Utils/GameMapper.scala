package Utils

import play.api.mvc.RequestHeader

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 15/05/2014
 * Time: 13:38
 */
object GameMapper {

  /** Facade for invalid requst */
  val invalidRequest = new RequestNotFound

  val STARTING_INDEX : Int = 1

  /**  Game Mapping Container; mutable reference to immutable map */
  var gameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])] = Map(1 -> (None,None))

  //TODO test insert
  def insertRequestIntoGameMapping(newRequest : RequestHeader) =
    gameMapping = updateCurrentGameMappingWithRequest(gameMapping,
      getNextFreeIndex(gameMapping,STARTING_INDEX)
      ,newRequest)


  //TODO test remove
  /** Replaces key -> (r1,r2) with key -> (None,None)
    *
    * @param finishedRequest - finished entity on client side
    * Replaces the whole tuple i.e. its associated player as well(!)
    * If finishedRequst is not found function will write (-1 -> (None,None))
    */
  def removeRequestFromGameMapping(finishedRequest : RequestHeader) =
    gameMapping.updated(findRequestTupleKey(finishedRequest.id), (None,None))


  /**
   *
   * @param request - the requst to be checked
   * @return true if request is ready i.e. has a partner; false otherwise
   */
  def isGameReadyWith(request : RequestHeader) : Boolean =
      requestHasPartnerIn(gameMapping(findRequestTupleKey(request.id)))


  /** Util functions for gameMapping */

  /** Given the current game mapping and current Index and return the tuple  */
  private def getCurrentRequestTuple(currentGameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])],
                             currentFreeIndex : Int)
  : (Option[RequestHeader],Option[RequestHeader])
  = currentGameMapping(currentFreeIndex)

  /** Given a request and the most recent request tuple -> create a new request tuple */
  private def createRequestTuple(request : RequestHeader ,
                         currentRequestTuple : (Option[RequestHeader],Option[RequestHeader]))
  : (Option[RequestHeader],Option[RequestHeader])
  = currentRequestTuple match {

    // if a space is free fill up tuple
    case (Some(existingRequest) , None) => (Some(existingRequest), Some(request))
    // if empty or full return new tuple
    case (_,_) => (Some(request), None)

  }

  /** Given the current game mapping and its current index -> return new current index
    * Linear Search through gameMapping -> could be optimized with list of no-longer-used keys
    * */
  private def getNextFreeIndex(currentGameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])],
                               currentIndex : Int)
  : Int
  = currentGameMapping(currentIndex) match {

    // if full keep searching
    case (Some(request1),Some(request2)) => getNextFreeIndex(currentGameMapping, currentIndex + 1)
    // if not full return current index
    case (_ , _) => currentIndex
  }

  /** Writes new request header into the current free index in the game mapping Map */
  private def updateCurrentGameMappingWithRequest(currentGameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])] ,
                                          currentFreeIndex : Int,
                                          newRequest : RequestHeader)
  : Map[Int, (Option[RequestHeader],Option[RequestHeader])]
  = currentGameMapping
    .updated(
      currentFreeIndex,
      createRequestTuple(
        newRequest,
        getCurrentRequestTuple(currentGameMapping,currentFreeIndex))
    )

  /** Given the id of a RequstHeader instance, find its mapping in gameMapping and return the key */
  private def findRequestTupleKey(requestId : Long) : Int =
    gameMapping.find(element => element._2._1.getOrElse(invalidRequest).id == requestId ||
                                element._2._2.getOrElse(invalidRequest).id == requestId)
    .getOrElse((-1,(None,None)))._1

  private def requestHasPartnerIn(requests : (Option[RequestHeader],Option[RequestHeader]))
  : Boolean = requests match {

    // if request has partner game can start
    case (Some(request),Some(otherRequest)) => true
    // else wait
    case (_ , _)                           => false

  }



}
