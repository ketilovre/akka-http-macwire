package com.ketilovre.server

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.ketilovre.config.ConfigModule
import com.ketilovre.server.routes.RouteModule
import com.ketilovre.server.wrappers.WrapperModule
import com.softwaremill.macwire.wire

import scala.concurrent.ExecutionContext

trait ServerModule extends RouteModule with WrapperModule with ConfigModule {

  def system: ActorSystem

  def dispatcher: ExecutionContext

  def materializer: ActorMaterializer

  lazy val server: Server = wire[Server]

  lazy val api: Api = wire[Api]

}
