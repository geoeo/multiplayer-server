package Utils

import play.api.mvc.RequestHeader

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 15/05/2014
 * Time: 13:38
 */
object GameMapper {

  /** Facade for invalid request i.e. not found in gameMapping */
  val invalidRequest = new RequestNotFound

  val STARTING_INDEX : Int = 1

  /**  Game Mapping Container; mutable reference to immutable map */
  var gameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])]
  = Map(1 -> (None,None))

  /**
   * Pre: It is assumed that the request does not already exist in gameMapping
   * @param newRequest - new request to be placed into the gameMapping
   *
   */
  def insertRequestIntoGameMapping(newRequest : RequestHeader)
  = gameMapping = updateCurrentGameMappingWithRequest(gameMapping,
                                                      getNextFreeIndex(gameMapping,STARTING_INDEX),
                                                      newRequest)

  /** Replaces key -> (r1,r2) with key -> (None,None)
    *
    * @param finishedRequest - finished entity on client side
    * Replaces the whole tuple i.e. its associated player as well(!)
    * If finishedRequst is not found function will write (-1 -> (None,None))
    */
  def removeRequestFromGameMapping(finishedRequest : RequestHeader)
  = gameMapping = gameMapping.updated(findRequestTupleKey(finishedRequest.id), (None,None))

  /**
   *
   * @param request - a request
   * @return the request opponent or invalidRequest object
   */
  def getOpponentOf(request : RequestHeader)
  : RequestHeader
  =
  {
    val playerTuple = getPlayerTupleOf(request)
    if (playerTuple._1.id == request.id) playerTuple._2 else playerTuple._1
  }

  /**
   *
   * @param request - the request to be checked
   * @return true if request is ready i.e. has a partner; false otherwise
   */
  def isGameReadyWith(request : RequestHeader)
  : Boolean
  = requestHasPartnerIn(gameMapping.getOrElse(findRequestTupleKey(request.id),(None,None)))

  /**
   *
   * @param request - a request
   * @return the player tuple or ( request , invalidRequest ) [or vice versa]
   */
  private def getPlayerTupleOf(request : RequestHeader)
  : (RequestHeader , RequestHeader)
  = ( gameMapping(findRequestTupleKey(request.id))._1.getOrElse(invalidRequest) ,
    gameMapping(findRequestTupleKey(request.id))._2.getOrElse(invalidRequest) )


  /** Util functions for gameMapping */

  /** Given the current game mapping and current Index and return the tuple  */
  private def getCurrentRequestTuple(currentGameMapping : Map[Int, (Option[RequestHeader],Option[RequestHeader])],
                                     currentFreeIndex : Int)
  : (Option[RequestHeader],Option[RequestHeader])
  = currentGameMapping.getOrElse(currentFreeIndex,(None,None))

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
  = currentGameMapping.getOrElse(currentIndex,(None,None)) match {

    // if full keep searching
    case (Some(_),Some(_)) => getNextFreeIndex(currentGameMapping, currentIndex + 1)
    // if not full return current index
    case (_,_) => currentIndex
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
  private def findRequestTupleKey(requestId : Long)
  : Int
  = gameMapping.find(element => element._2._1.getOrElse(invalidRequest).id == requestId ||
                                element._2._2.getOrElse(invalidRequest).id == requestId)
    .getOrElse((-1,(None,None)))._1

  private def requestHasPartnerIn(requests : (Option[RequestHeader],Option[RequestHeader]))
  : Boolean
  = requests match {

    // if request has partner game can start
    case (Some(request),Some(otherRequest)) => true
    // else wait
    case ( _ , _ )                          => false

  }



}
