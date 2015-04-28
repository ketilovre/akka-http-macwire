package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RoutingSettings, RoutingSetup}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow
import com.ketilovre.config.ApiParallelismConfig
import com.softwaremill.macwire.Tagging.@@

import scala.concurrent.ExecutionContext

class Api(partialRoutes: Seq[PartialRoute], wrappers: Seq[Wrapper], parallelism: Int @@ ApiParallelismConfig)
         (implicit sys: ActorSystem, mat: ActorFlowMaterializer, ec: ExecutionContext) {

  implicit private val routingSettings = RoutingSettings.default

  implicit private val routingSetup: RoutingSetup = RoutingSetup.apply

  private val concatenatedWrappers: Route => Route = wrappers match {
    case Nil     => Route.apply
    case x :: xs => xs.foldLeft(x.wrap _) { (builder, wrapper) =>
      builder.compose(wrapper.wrap)
    }
  }

  private val concatenatedRoutes: Route = partialRoutes match {
    case Nil     => complete(OK)
    case x :: xs => xs.foldLeft(x.route) { (builder, partial) =>
      builder ~ partial.route
    }
  }

  val route: Route = {
    concatenatedWrappers {
      concatenatedRoutes
    }
  }

  val routeFlow: Flow[HttpRequest, HttpResponse, Unit] = {
    Flow[HttpRequest].mapAsync(parallelism, Route.asyncHandler(route))
  }
}
