package com.ketilovre.server.routes

import akka.http.scaladsl.server.Route
import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.GreetHandler

import scala.concurrent.ExecutionContext

class GreetRoute(handler: GreetHandler)(implicit ec: ExecutionContext) extends PartialRoute {

  val route: Route = {
    get {
      path("greet" / Segment ~ PathEnd) { name =>
        complete(handler.hello(name))
      }
    }
  }
}
