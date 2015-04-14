package com.ketilovre.server

import com.ketilovre.akka.AkkaModule
import com.ketilovre.server.routes.RouteModule
import com.softwaremill.macwire.Macwire

trait ServerModule extends Macwire with RouteModule {
  self: AkkaModule =>

  lazy val server: Server = wire[Server]

  lazy val api: Api = wire[Api]

}
