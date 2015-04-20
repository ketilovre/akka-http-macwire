package server

import akka.http.model.StatusCodes._
import akka.http.model.headers._
import akka.http.server.{Directives, Route}
import com.ketilovre.server.{Api, PartialRoute, Wrapper}
import helpers.RouteSpec

class ApiSpec extends RouteSpec {

  "a default wrapper and route should be inserted if one or neither is defined" in {

    val api = new Api(Seq.empty, Seq.empty)

    Get("/") ~> api.route ~> check {
      status mustEqual OK
    }
  }

  "concatenatedRoutes" should {

    "use the first route directly if only one is defined" in {

      object SingleRoute extends PartialRoute {

        val route: Route = {
          path("single") {
            complete(OK)
          }
        }
      }

      val api = new Api(Seq(SingleRoute), Seq.empty)

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

      val successApi = new Api(Seq(SuccessRoute, FailureRoute), Seq.empty)
      val failureApi = new Api(Seq(FailureRoute, SuccessRoute), Seq.empty)

      Get("/multiple") ~> successApi.route ~> check {
        status mustEqual OK
      }

      Get("/multiple") ~> failureApi.route ~> check {
        status mustEqual InternalServerError
      }
    }
  }

  "concatenatedWrappers" should {

    "use the first wrapper directly if only one is defined" in {

      object SingleWrapper extends Wrapper with Directives {
        def wrap(route: Route): Route = {
          respondWithDefaultHeader(`Accept-Language`(LanguageRange.*)) {
            route
          }
        }
      }

      val api = new Api(Seq.empty, Seq(SingleWrapper))

      Get("/") ~> api.route ~> check {
        status mustEqual OK
        response.headers.head must beAnInstanceOf[`Accept-Language`]
      }
    }

    "concatenate the wrappers if multiple are defined, retaining the order of wrappers" in {

      object FirstWrapper extends Wrapper with Directives {
        def wrap(route: Route): Route = {
          respondWithHeader(`Accept-Language`(Language("en"))) {
            route
          }
        }
      }

      object SecondWrapper extends Wrapper with Directives {
        def wrap(route: Route): Route = {
          respondWithHeader(`Accept-Language`(Language("no"))) {
            route
          }
        }
      }

      val ascendingApi = new Api(Seq.empty, Seq(FirstWrapper, SecondWrapper))
      val descendingApi = new Api(Seq.empty, Seq(SecondWrapper, FirstWrapper))

      Get("/") ~> ascendingApi.route ~> check {
        status mustEqual OK
        response.headers.head mustEqual `Accept-Language`(Language("en"))
      }

      Get("/") ~> descendingApi.route ~> check {
        status mustEqual OK
        response.headers.head mustEqual `Accept-Language`(Language("no"))
      }
    }
  }
}
