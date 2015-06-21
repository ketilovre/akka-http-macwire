package com.ketilovre.server.routes

import akka.http.scaladsl.server.Route
import com.ketilovre.server.handlers.SocketHandler

class SocketRoute(handler: SocketHandler) extends PartialRoute {

  val route: Route = {
    path("socket") {
      handleWebsocketMessages(handler.hello)
    }
  }
}
