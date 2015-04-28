package com.ketilovre.akka

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer

import scala.concurrent.ExecutionContext

trait AkkaModule {

  implicit val system: ActorSystem = ActorSystem("akka-http-macwire")

  implicit val dispatcher: ExecutionContext = system.dispatcher

  implicit val materializer: ActorFlowMaterializer = ActorFlowMaterializer()

}
