package test

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import controllers.Application
import org.specs2.specification.Scope
;

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */


/**
 * Equivalent to @Before
 */
trait fakeRegistryApplication extends Scope {

  def initRegistry() = Application.registry = Map()
  initRegistry()

}
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

    "adding too registry" in new fakeRegistryApplication {

      Application.registry.getOrElse(1,false) must beFalse

      Application.registry = Application.registry.updated(1,true)

      Application.registry.getOrElse(1,false) must beTrue

    }

    "adding the leaving the registry" in new fakeRegistryApplication {

      Application.registry.getOrElse(1,false) must beFalse

      Application.registry = Application.registry.updated(1,true)

      Application.registry.getOrElse(1,false) must beTrue

      Application.registry = Application.registry.updated(1,false)

      Application.registry.getOrElse(1,false) must beFalse

    }
  }

}


