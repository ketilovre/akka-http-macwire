package com.ketilovre.server.routes

import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.HandlerModule
import com.softwaremill.macwire.Macwire

import scala.concurrent.ExecutionContext

trait RouteModule extends Macwire with HandlerModule {

  def dispatcher: ExecutionContext

  lazy val routes: Seq[PartialRoute] = Seq(
    helloRoute
  )

  lazy val helloRoute: HelloRoute = wire[HelloRoute]
}
