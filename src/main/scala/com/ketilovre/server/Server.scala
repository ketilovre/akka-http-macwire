package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.ketilovre.config.{ServerHostConfig, ServerPortConfig}
import com.softwaremill.macwire.Tagging.@@
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class Server(api: Api, host: String @@ ServerHostConfig, port: Int @@ ServerPortConfig)
            (implicit ac: ActorSystem, afm: ActorMaterializer, ec: ExecutionContext) {

  private val logger = LoggerFactory.getLogger("server")

  def bind: Future[ServerBinding] = {
    Http(ac).bindAndHandle(api.routeFlow, host, port)
  }

  def afterStart(binding: ServerBinding): Unit = {
    logger.info(s"Server started on ${binding.localAddress.toString}")
  }

  def beforeStop(binding: ServerBinding): Unit = {
    Await.ready({
      binding.unbind().map { _ =>
        ac.shutdown()
        ac.awaitTermination()
        logger.info("Shutting down")
      }
    }, 1.minute)
  }
}
