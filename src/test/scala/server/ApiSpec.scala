package server

import akka.http.model.StatusCodes._
import akka.http.model.headers.HttpEncodings._
import akka.http.model.headers.`Accept-Encoding`
import akka.http.server.Route
import com.ketilovre.server.{Api, PartialRoute}
import helpers.RouteSpec

class ApiSpec extends RouteSpec {

  "router" should {

    val api = new Api(Seq.empty)

    "apply encoding if requested" in {

      Get("/") ~> api.route ~> check {
        response.encoding mustEqual identity
      }

      Get("/") ~> `Accept-Encoding`(deflate) ~> api.route ~> check {
        response.encoding mustEqual deflate
      }

      Get("/") ~> `Accept-Encoding`(gzip, deflate) ~> api.route ~> check {
        response.encoding mustEqual gzip
      }
    }
  }

  "concatenatedRoutes" should {

    "insert a default route if no routes are defined" in {

      val api = new Api(Seq.empty)

      Get("/") ~> api.route ~> check {
        status mustEqual OK
      }
    }

    "use the first route directly if only one is defined" in {

      object SingleRoute extends PartialRoute {

        val route: Route = {
          path("single") {
            complete(OK)
          }
        }
      }

      val api = new Api(Seq(SingleRoute))

      Get("/single") ~> api.route ~> check {
        status mustEqual OK
      }
    }

    "concatenate the routes if multiple are defined, retaining the order of routes" in {

      object SuccessRoute extends PartialRoute {

        val route: Route = {
          path("multiple") {
            complete(OK)
          }
        }
      }

      object FailureRoute extends PartialRoute {

        val route: Route = {
          path("multiple") {
            complete(InternalServerError)
          }
        }
      }

      val successApi = new Api(Seq(SuccessRoute, FailureRoute))
      val failureApi = new Api(Seq(FailureRoute, SuccessRoute))

      Get("/multiple") ~> successApi.route ~> check {
        status mustEqual OK
      }

      Get("/multiple") ~> failureApi.route ~> check {
        status mustEqual InternalServerError
      }
    }
  }
}
