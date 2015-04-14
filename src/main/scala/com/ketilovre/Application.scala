package com.ketilovre

import com.ketilovre.akka.AkkaModule
import com.ketilovre.server.ServerModule
import org.slf4j.LoggerFactory

object Application extends App with AkkaModule with ServerModule {

  server.boot.map { running =>

    val logger = LoggerFactory.getLogger("server")

    logger.info(s"Server started on ${running.localAddress.toString}")

    sys.addShutdownHook {
      logger.info("Shutting down application")
      running.unbind().map { _ =>
        system.awaitTermination()
      }
    }
  }
}
