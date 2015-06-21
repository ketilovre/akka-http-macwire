package com.ketilovre.server.routes

import akka.http.scaladsl.server.{Directives, Route}
import com.ketilovre.server.handlers.HandlerModule
import com.softwaremill.macwire.wire

trait PartialRoute extends Directives {
  def route: Route
}

trait RouteModule extends HandlerModule {

  lazy val routes: Seq[PartialRoute] = Seq(
    wire[GreetRoute],
    wire[StreamingRoute],
    wire[SocketRoute]
  )
}
