package com.ketilovre.server.wrappers

import akka.http.model.HttpHeader
import akka.http.model.headers.RawHeader
import akka.http.server.Directives._
import akka.http.server.Route
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
