package com.ketilovre

import com.ketilovre.akka.AkkaModule
import com.ketilovre.server.ServerModule

object Application extends App with AkkaModule with ServerModule {

  server.bind.foreach { binding =>
    server.afterStart(binding)
    sys.addShutdownHook {
      server.beforeStop(binding)
    }
  }
}
