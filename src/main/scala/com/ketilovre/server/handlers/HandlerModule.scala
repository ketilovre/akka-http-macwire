package com.ketilovre.server.handlers

import com.ketilovre.akka.AkkaModule
import com.softwaremill.macwire.Macwire

trait HandlerModule extends Macwire {
  self: AkkaModule =>

  val helloHandler: HelloHandler = wire[HelloHandler]

}
