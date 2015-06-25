package com.ketilovre.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait AkkaModule {

  implicit val system: ActorSystem = ActorSystem()

  implicit val dispatcher: ExecutionContext = system.dispatcher

  implicit val materializer: ActorMaterializer = ActorMaterializer()

}
