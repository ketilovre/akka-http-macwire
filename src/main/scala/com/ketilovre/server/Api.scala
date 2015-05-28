package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, RoutingSettings, RoutingSetup}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext

class Api(partialRoutes: Seq[PartialRoute], wrappers: Seq[Wrapper])
         (implicit sys: ActorSystem, mat: ActorFlowMaterializer, ec: ExecutionContext) {

  implicit private val routingSettings = RoutingSettings.default

  implicit private val routingSetup: RoutingSetup = RoutingSetup.apply

  private val allWrappers = {
    wrappers.foldLeft[Route => Route](Route.apply) { (builder, wrapper) =>
      builder.compose(wrapper.wrap)
    }
  }

  private val allRoutes = {
    partialRoutes.foldRight[Route](reject) { (partial, builder) =>
      partial.route ~ builder
    }
  }

  val route: Route = allWrappers(allRoutes)

  val routeFlow: Flow[HttpRequest, HttpResponse, Unit] = Route.handlerFlow(route)

}
