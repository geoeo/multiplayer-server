import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.mvc.{Headers, RequestHeader}
import play.api.test._
import play.api.test.Helpers._
import Utils.GameMapper

/**
 * Created with IntelliJ IDEA.
 * User: marchaubenstock
 * Date: 27/05/2014
 * Time: 22:15
 */
@RunWith(classOf[JUnitRunner])
class GamerMapperSpec extends Specification {

  "Game Mapper" should {

    "Initial Game Mapping" in new WithApplication {

      GameMapper.gameMapping must have size 1

    }

    "Adding a Request to Game Mapping " in new WithApplication {

      val testRequest : RequestHeader = new RequestHeader {
        override def id: Long = ???

        override def tags: Map[String, String] = ???

        override def version: String = ???

        override def path: String = ???

        override def headers: Headers = ???

        override def method: String = ???

        override def queryString: Map[String, Seq[String]] = ???

        override def remoteAddress: String = ???

        override def uri: String = ???
      }

    }

  }

}
