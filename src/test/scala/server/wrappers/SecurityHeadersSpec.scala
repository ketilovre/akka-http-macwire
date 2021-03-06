package server.wrappers

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import com.ketilovre.server.wrappers.SecurityHeaders
import helpers.{BaseSpec, RouteSpec}

class SecurityHeadersSpec extends BaseSpec with RouteSpec {

  val security = new SecurityHeaders()

  val route = security.wrap(complete(""))

  "SecurityHeaders" should {

    "add default security headers to all requests" in {

      Options("/") ~> route ~> check {
        response.headers must containAllOf(List(
          RawHeader("X-Frame-Options", "DENY"),
          RawHeader("X-Content-Type-Options", "nosniff"),
          RawHeader("Content-Security-Policy", "default-src 'self'")
        ))
      }
    }
  }
}
