package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.server._
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Sink

import scala.concurrent.{ExecutionContext, Future}

class Server(routes: Api)(implicit sys: ActorSystem, mat: ActorFlowMaterializer, ec: ExecutionContext) {

  implicit private val routingSettings = RoutingSettings.default

  implicit private val routingSetup: RoutingSetup = RoutingSetup.apply

  def boot: Future[Http.ServerBinding] = {
    Http(sys).bind(interface = "localhost", port = 8080).to(Sink.foreach { connection =>
      connection handleWith Route.handlerFlow(routes.route)
    }).run()
  }
}
