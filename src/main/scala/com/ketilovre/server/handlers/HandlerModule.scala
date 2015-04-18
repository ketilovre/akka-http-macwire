package com.ketilovre.server.handlers

import com.softwaremill.macwire.Macwire

trait HandlerModule extends Macwire {

  lazy val helloHandler: HelloHandler = wire[HelloHandler]

}
