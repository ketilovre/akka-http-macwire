package com.ketilovre.server.handlers

import com.softwaremill.macwire.wire

trait HandlerModule {

  lazy val greetHandler: GreetHandler = wire[GreetHandler]

  lazy val socketHandler: SocketHandler = wire[SocketHandler]

  lazy val streamingHandler: StreamingHandler = wire[StreamingHandler]

}
