package com.ketilovre.server.wrappers

import akka.http.model.HttpHeader
import akka.http.model.StatusCodes.OK
import akka.http.model.headers._
import akka.http.server.Directives._
import akka.http.server.Route
import com.ketilovre.server.Wrapper

class CORSHeaders extends Wrapper {

  private val maxAge = 3600L

  private val accessControlRequestMethod = "Access-Control-Request-Method"

  private val accessControlRequestHeaders = "Access-Control-Request-Headers"

  private val standardHeaders: List[HttpHeader] = List(
    `Access-Control-Allow-Origin`.apply(HttpOriginRange.*): HttpHeader,
    `Access-Control-Allow-Credentials`(allow = true)
  )

  def wrap(route: Route): Route = {
    options {
      optionalHeaderValueByName(accessControlRequestMethod) { requestMethod =>
        optionalHeaderValueByName(accessControlRequestHeaders) { requestHeaders =>
          val methods = requestMethod.map(RawHeader(accessControlRequestMethod, _))
          val headers = requestHeaders.map(RawHeader(accessControlRequestHeaders, _))
          respondWithDefaultHeaders(concatHeaders(methods, headers): _*) {
            respondWithDefaultHeader(`Access-Control-Max-Age`(maxAge)) {
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
