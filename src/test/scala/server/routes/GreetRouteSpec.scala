package server.routes

import akka.http.scaladsl.model.StatusCodes.{NotFound, OK}
import akka.http.scaladsl.server.Route
import com.ketilovre.server.handlers.GreetHandler
import com.ketilovre.server.routes.GreetRoute
import helpers.{BaseSpec, RouteSpec}

class GreetRouteSpec extends BaseSpec with RouteSpec {

  val handler = mock[GreetHandler]

  val route = Route.seal(new GreetRoute(handler).route)

  "HelloRoute" should {

    "reject if the name parameter is missing" in {

      Get("/greet") ~> route ~> check {
        status mustEqual NotFound
      }
    }

    "reply with a greeting if a name is passed" in prop { name: String =>

      handler.hello(name) returns name

      Get(s"/greet/$name") ~> route ~> check {
        if (name.isEmpty) {
          status mustEqual NotFound
        } else {
          status mustEqual OK
          entityAs[String] mustEqual name
        }
      }
    }
  }
}
