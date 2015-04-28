package com.ketilovre.server.handlers

import com.softwaremill.macwire.wire

trait HandlerModule {

  lazy val helloHandler: HelloHandler = wire[HelloHandler]

  lazy val socketHandler: SocketHandler = wire[SocketHandler]

}
