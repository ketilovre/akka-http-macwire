package com.ketilovre.server.wrappers

import akka.http.server.Directives._
import akka.http.server.Route
import com.ketilovre.server.Wrapper

class Encoding extends Wrapper {

  def wrap(route: Route): Route = {
    decodeRequest {
      encodeResponse {
        route
      }
    }
  }
}
