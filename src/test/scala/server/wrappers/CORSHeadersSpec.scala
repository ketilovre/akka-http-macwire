package server.wrappers

import akka.http.scaladsl.model.headers.{RawHeader, `Access-Control-Request-Headers`, `Access-Control-Request-Method`}
import akka.http.scaladsl.model.{HttpMethods, StatusCodes}
import akka.http.scaladsl.server.Directives._
import com.ketilovre.server.wrappers.CorsHeaders
import helpers.{BaseSpec, RouteSpec}

class CorsHeadersSpec extends BaseSpec with RouteSpec {

  val cors = new CorsHeaders()

  val route = cors.wrap(complete(""))

  "CORSHeaders" should {

    "OPTIONS requests" should {

      "always be answered by the wrapper, regardless of other routes" in {

        val errorRoute = cors.wrap(complete(StatusCodes.InternalServerError))

        Options("/") ~> errorRoute ~> check {
          response.status mustEqual StatusCodes.OK
        }
      }

      "always be answered by default headers" in {

        Options("/") ~> route ~> check {
          response.headers must containAllOf(Seq(
            RawHeader("Access-Control-Allow-Origin", "*"),
            RawHeader("Access-Control-Allow-Credentials", "true"),
            RawHeader("Access-Control-Max-Age", "3600")
          ))
        }
      }

      "respond to access-control-request-headers" in {

        Options("/") ~> `Access-Control-Request-Headers`("Content-Type") ~> route ~> check {
          response.headers must contain(`Access-Control-Request-Headers`("Content-Type"))
        }
      }

      "respond to access-control-request-method" in {

        Options("/") ~> `Access-Control-Request-Method`(HttpMethods.GET) ~> route ~> check {
          response.headers must contain(`Access-Control-Request-Method`(HttpMethods.GET))
        }
      }

      "respond to both access-control-request-headers and access-control-request-method" in {
        Options("/") ~> `Access-Control-Request-Headers`("Content-Type") ~>
          `Access-Control-Request-Method`(HttpMethods.GET) ~> route ~> check {
          response.headers must contain(`Access-Control-Request-Headers`("Content-Type"))
          response.headers must contain(`Access-Control-Request-Method`(HttpMethods.GET))
        }
      }
    }

    "other requests" should {

      "be decorated with default headers" in {

        Get("/") ~> route ~> check {
          response.status mustEqual StatusCodes.OK
          response.headers must containAllOf(Seq(
            RawHeader("Access-Control-Allow-Origin", "*"),
            RawHeader("Access-Control-Allow-Credentials", "true")
          ))
        }
      }
    }
  }
}
