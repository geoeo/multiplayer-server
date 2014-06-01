package test

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.mvc.{Headers, RequestHeader}
import play.api.test._
import Utils.GameMapper
import org.specs2.specification.Scope

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 27/05/2014
 * Time: 22:15
 */

/**
 * Equivalent to @Before
 */
trait fakeApplication extends Scope {

  def initMapper() = GameMapper.gameMapping = Map(1 -> (None,None))
  initMapper()

}

@RunWith(classOf[JUnitRunner])
class GameMapperSpec extends Specification {

  val testRequest : RequestHeader = new RequestHeader {
    override def id: Long = 1

    override def tags: Map[String, String] = ???

    override def version: String = ???

    override def path: String = ???

    override def headers: Headers = ???

    override def method: String = ???

    override def queryString: Map[String, Seq[String]] = ???

    override def remoteAddress: String = ???

    override def uri: String = ???
  }

  val testRequest2 : RequestHeader = new RequestHeader {
    override def id: Long = 2

    override def tags: Map[String, String] = ???

    override def version: String = ???

    override def path: String = ???

    override def headers: Headers = ???

    override def method: String = ???

    override def queryString: Map[String, Seq[String]] = ???

    override def remoteAddress: String = ???

    override def uri: String = ???
  }

  val testRequest3 : RequestHeader = new RequestHeader {
    override def id: Long = 3

    override def tags: Map[String, String] = ???

    override def version: String = ???

    override def path: String = ???

    override def headers: Headers = ???

    override def method: String = ???

    override def queryString: Map[String, Seq[String]] = ???

    override def remoteAddress: String = ???

    override def uri: String = ???
  }

  "Game Mapper" should {

    "Initial Game Mapping" in new fakeApplication {

      GameMapper.gameMapping must have size 1

      GameMapper.gameMapping(1) mustEqual (None,None)

    }

    "Adding/Removing a Request to Game Mapping " in new fakeApplication {


      GameMapper.insertRequestIntoGameMapping(testRequest)

      GameMapper.gameMapping must have size 1

      GameMapper.gameMapping(1) mustEqual (Some(testRequest), None)

      GameMapper.removeRequestFromGameMapping(testRequest)

      GameMapper.gameMapping must have size 1

      GameMapper.gameMapping(1) mustEqual (None,None)

    }

    "Removing a request that is not found" in new fakeApplication {

      GameMapper.removeRequestFromGameMapping(testRequest)

      GameMapper.gameMapping must have size 2

      GameMapper.gameMapping(-1) mustEqual (None,None)


    }

    "Adding two Requests" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)
      GameMapper.insertRequestIntoGameMapping(testRequest2)

      GameMapper.gameMapping must have size 1

      GameMapper.gameMapping(1) mustEqual (Some(testRequest), Some(testRequest2))

    }

    "Adding two Request other way around" in new fakeApplication {


      GameMapper.insertRequestIntoGameMapping(testRequest2)
      GameMapper.insertRequestIntoGameMapping(testRequest)

      GameMapper.gameMapping must have size 1

      GameMapper.gameMapping(1) mustEqual (Some(testRequest2), Some(testRequest))

    }

    "Adding three requests" in new fakeApplication {

        GameMapper.insertRequestIntoGameMapping(testRequest)
        GameMapper.insertRequestIntoGameMapping(testRequest2)
        GameMapper.insertRequestIntoGameMapping(testRequest3)

        GameMapper.gameMapping must have size 2

        GameMapper.gameMapping(1) mustEqual (Some(testRequest),Some(testRequest2))
        GameMapper.gameMapping(2) mustEqual (Some(testRequest3),None)
    }

    "Adding three requests; removing first" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)
      GameMapper.insertRequestIntoGameMapping(testRequest2)
      GameMapper.insertRequestIntoGameMapping(testRequest3)

      GameMapper.removeRequestFromGameMapping(testRequest)

      GameMapper.gameMapping must have size 2

      GameMapper.gameMapping(1) mustEqual (None,None)
      GameMapper.gameMapping(2) mustEqual (Some(testRequest3),None)

    }

    "Adding three requests; removing second; adding second" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)
      GameMapper.insertRequestIntoGameMapping(testRequest2)
      GameMapper.insertRequestIntoGameMapping(testRequest3)

      GameMapper.removeRequestFromGameMapping(testRequest2)

      GameMapper.gameMapping must have size 2

      GameMapper.gameMapping(1) mustEqual (None,None)
      GameMapper.gameMapping(2) mustEqual (Some(testRequest3),None)

      GameMapper.insertRequestIntoGameMapping(testRequest2)

      GameMapper.gameMapping must have size 2

      GameMapper.gameMapping(1) mustEqual (Some(testRequest2),None)


    }

    "Getting Oppoinent of a request" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)
      GameMapper.insertRequestIntoGameMapping(testRequest2)

      GameMapper.getOpponentOf(testRequest) must not beNull;
      GameMapper.getOpponentOf(testRequest).id mustEqual 2



    }

    "Getting Opponent of request with no opponent" in new fakeApplication{

      GameMapper.insertRequestIntoGameMapping(testRequest)

      GameMapper.getOpponentOf(testRequest) must not beNull;
      GameMapper.getOpponentOf(testRequest).id mustEqual -1

    }

    "Getting Opponnent of invalid Request" in new fakeApplication{

      GameMapper.getOpponentOf(testRequest) must not beNull;
      GameMapper.getOpponentOf(testRequest).id mustEqual -1;
    }


    "Game is ready with no opponent" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)

      GameMapper.isGameReadyWith(testRequest) must beFalse

    }

    "Game is ready with an opponent" in new fakeApplication {

      GameMapper.insertRequestIntoGameMapping(testRequest)
      GameMapper.insertRequestIntoGameMapping(testRequest2)
      GameMapper.insertRequestIntoGameMapping(testRequest3)

      GameMapper.isGameReadyWith(testRequest) must beTrue
      GameMapper.isGameReadyWith(testRequest2) must beTrue
      GameMapper.isGameReadyWith(testRequest3) must beFalse

    }

    "Game is ready with an invalid request" in new fakeApplication {

      GameMapper.isGameReadyWith(testRequest) must beFalse
      GameMapper.isGameReadyWith(testRequest2) must beFalse
      GameMapper.isGameReadyWith(testRequest3) must beFalse

    }

  }

}
