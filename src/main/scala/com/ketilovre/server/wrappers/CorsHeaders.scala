package com.ketilovre.server.wrappers

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.ketilovre.server.Wrapper

class CORSHeaders extends Wrapper {

  private val accessControlRequestMethod = "Access-Control-Request-Method"

  private val accessControlRequestHeaders = "Access-Control-Request-Headers"

  private val maxAgeHeader = RawHeader("Access-Control-Max-Age", "3600")

  private val standardHeaders: List[HttpHeader] = List(
    RawHeader("Access-Control-Allow-Origin", "*"),
    RawHeader("Access-Control-Allow-Credentials", "true")
  )

  def wrap(route: Route): Route = {
    options {
      optionalHeaderValueByName(accessControlRequestMethod) { requestMethod =>
        optionalHeaderValueByName(accessControlRequestHeaders) { requestHeaders =>
          val methods = requestMethod.map(RawHeader(accessControlRequestMethod, _))
          val headers = requestHeaders.map(RawHeader(accessControlRequestHeaders, _))
          respondWithDefaultHeaders(concatHeaders(methods, headers): _*) {
            respondWithDefaultHeader(maxAgeHeader) {
              complete(OK)
            }
          }
        }
      }
    } ~ {
      respondWithDefaultHeaders(standardHeaders: _*) {
        route
      }
    }
  }

  private def concatHeaders(methods: Option[RawHeader], headers: Option[RawHeader]): List[HttpHeader] = {
    (methods, headers) match {
      case (Some(m), Some(h)) => h :: m :: standardHeaders
      case (Some(m), None)    => m :: standardHeaders
      case (None, Some(h))    => h :: standardHeaders
      case _                  => standardHeaders
    }
  }
}
