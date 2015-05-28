package server.routes

import com.ketilovre.server.handlers.GreetHandler
import com.ketilovre.server.routes.GreetRoute
import helpers.{BaseSpec, RouteSpec}

class GreetRouteSpec extends BaseSpec with RouteSpec {

  val handler = mock[GreetHandler]

  val route = new GreetRoute(handler).route

  "HelloRoute" should {

    "reject if the name parameter is missing" in {

      Get("/greet") ~> route ~> check {
        rejections mustEqual Nil
      }
    }

    "reply with a greeting if a name is passed" in prop { name: String =>

      handler.hello(name) returns name

      Get(s"/greet/$name") ~> route ~> check {
        if (name.isEmpty) {
          rejections mustEqual Nil
        } else {
          entityAs[String] mustEqual name
        }
      }
    }
  }
}
