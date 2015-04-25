package server.routes

import akka.http.scaladsl.server.MissingQueryParamRejection
import com.ketilovre.server.handlers.HelloHandler
import com.ketilovre.server.routes.HelloRoute
import helpers.RouteSpec

class HelloRouteSpec extends RouteSpec {

  val handler = mock[HelloHandler]

  val route = new HelloRoute(handler).route

  "/(?name=*)" should {

    "reject if the name parameter is missing" in {

      Get("/") ~> route ~> check {
        rejection must beAnInstanceOf[MissingQueryParamRejection]
      }
    }

    "reply with a greeting if a name is passed" in prop { name: String =>

      handler.hello(name) returns name

      Get(s"/?name=$name") ~> route ~> check {
        entityAs[String] mustEqual name
      }
    }
  }
}
