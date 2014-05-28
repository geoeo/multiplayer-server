package test

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import controllers.Application
import play.api.mvc.WebSocket
import Utils.GameMapper
import org.webbitserver.netty.WebSocketClient

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }

    "connecting to lobby" in new WithApplication{

      val fakeRequest = FakeRequest(GET,"/lobbySocket")

      route(fakeRequest)

      GameMapper.gameMapping must have size 1
      GameMapper.gameMapping(1) mustEqual (Some(fakeRequest),None)
    }
  }
}
