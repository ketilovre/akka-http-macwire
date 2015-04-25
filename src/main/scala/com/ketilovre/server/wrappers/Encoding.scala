package com.ketilovre.server.wrappers

import akka.http.scaladsl.server.Directives.{decodeRequest, encodeResponse}
import akka.http.scaladsl.server.Route
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
