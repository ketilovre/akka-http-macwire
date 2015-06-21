package com.ketilovre.server.wrappers

import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class CorsHeaders extends Wrapper {

  private val acrm = "access-control-request-method"

  private val acrh = "access-control-request-headers"

  private val maxAgeHeader = RawHeader("Access-Control-Max-Age", "3600")

  private val standardHeaders = List(
    RawHeader("Access-Control-Allow-Origin", "*"),
    RawHeader("Access-Control-Allow-Credentials", "true")
  )

  def wrap(route: Route): Route = {
    respondWithDefaultHeaders(standardHeaders) {
      options {
        extract(ctx => ctx.request.headers) { headers =>
          respondWithDefaultHeaders(headers.filter(h => h.is(acrm) || h.is(acrh))) {
            respondWithDefaultHeader(maxAgeHeader) {
              complete(OK)
            }
          }
        }
      } ~ {
        route
      }
    }
  }
}
