package com.ketilovre.server.routes

import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.HandlerModule
import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext

trait RouteModule extends HandlerModule {

  def dispatcher: ExecutionContext

  lazy val routes: Seq[PartialRoute] = Seq(
    wire[GreetRoute],
    wire[StreamingRoute],
    wire[SocketRoute]
  )
}
