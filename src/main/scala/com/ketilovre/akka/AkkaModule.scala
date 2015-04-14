package com.ketilovre.akka

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorFlowMaterializer
import com.softwaremill.macwire.Macwire

import scala.concurrent.ExecutionContext

trait AkkaModule extends Macwire {

  implicit val system: ActorSystem = ActorSystem("akka-http-macwire")

  implicit val dispatcher: ExecutionContext = system.dispatcher

  implicit val materializer: ActorFlowMaterializer = ActorFlowMaterializer()

}
