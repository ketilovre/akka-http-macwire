package com.ketilovre.server.routes

import akka.http.scaladsl.server.Route
import com.ketilovre.server.handlers.GreetHandler

class GreetRoute(handler: GreetHandler) extends PartialRoute {

  val route: Route = {
    get {
      path("greet" /  Segment) { name =>
        complete(handler.hello(name))
      }
    }
  }
}
