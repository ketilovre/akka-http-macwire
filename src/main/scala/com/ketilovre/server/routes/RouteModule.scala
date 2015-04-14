package com.ketilovre.server.routes

import com.ketilovre.akka.AkkaModule
import com.ketilovre.server.handlers.HandlerModule
import com.softwaremill.macwire.Macwire

trait RouteModule extends Macwire with HandlerModule {
  self: AkkaModule =>

  lazy val routes: Seq[PartialRoute] = {
    Seq(
      helloRoute
    )
  }

  val helloRoute: HelloRoute = wire[HelloRoute]
}
