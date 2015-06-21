package com.ketilovre.server.wrappers

import akka.http.scaladsl.server.Directives.{decodeRequest, encodeResponse}
import akka.http.scaladsl.server.Route

class Encoding extends Wrapper {

  def wrap(route: Route): Route = {
    decodeRequest {
      encodeResponse {
        route
      }
    }
  }
}
