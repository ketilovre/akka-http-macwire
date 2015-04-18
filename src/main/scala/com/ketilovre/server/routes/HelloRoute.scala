package com.ketilovre.server.routes

import akka.http.server.Route
import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.HelloHandler

import scala.concurrent.ExecutionContext

class HelloRoute(helloHandler: HelloHandler)(implicit ec: ExecutionContext) extends PartialRoute {

  def route: Route = {
    get {
      path("") {
        parameter("name") { name =>
          complete(helloHandler.hello(name))
        }
      }
    }
  }
}
