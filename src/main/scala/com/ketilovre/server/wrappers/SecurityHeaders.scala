package com.ketilovre.server.wrappers

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.respondWithDefaultHeaders
import akka.http.scaladsl.server.Route
import com.ketilovre.server.Wrapper

class SecurityHeaders extends Wrapper {

  private val securityHeaders: Seq[HttpHeader] = Seq(
    RawHeader("X-Frame-Options", "DENY"),
    RawHeader("X-Content-Type-Options", "nosniff"),
    RawHeader("Content-Security-Policy", "default-src 'self'")
  )

  def wrap(route: Route): Route = {
    respondWithDefaultHeaders(securityHeaders: _*) {
      route
    }
  }
}
