package com.ketilovre.server

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import com.ketilovre.config.ConfigModule
import com.ketilovre.server.routes.RouteModule
import com.ketilovre.server.wrappers.WrapperModule
import com.softwaremill.macwire.Macwire

import scala.concurrent.ExecutionContext

trait ServerModule extends Macwire with RouteModule with WrapperModule with ConfigModule {

  def system: ActorSystem

  def dispatcher: ExecutionContext

  def materializer: ActorFlowMaterializer

  lazy val server: Server = wire[Server]

  lazy val api: Api = wire[Api]

}
