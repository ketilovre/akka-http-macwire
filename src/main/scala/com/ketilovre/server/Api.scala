package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.model.{HttpRequest, HttpResponse}
import akka.http.server.Directives._
import akka.http.server.{Route, RoutingSettings, RoutingSetup}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext

class Api(partialRoutes: Seq[PartialRoute], wrappers: Seq[Wrapper])
         (implicit sys: ActorSystem, mat: ActorFlowMaterializer, ec: ExecutionContext) {

  implicit private val routingSettings = RoutingSettings.default

  implicit private val routingSetup: RoutingSetup = RoutingSetup.apply

  lazy val routeFlow: Flow[HttpRequest, HttpResponse, Unit] = Route.handlerFlow(route)

  lazy val route: Route = {
    concatenatedWrappers {
      concatenatedRoutes
    }
  }

  private val concatenatedRoutes: Route = partialRoutes match {
    case Nil     => complete("Please add some routes")
    case x :: xs => xs.foldLeft(x.route) { (builder, partial) =>
      builder ~ partial.route
    }
  }

  private val concatenatedWrappers: Route => Route = wrappers match {
    case Nil     => Route.apply
    case x :: xs => xs.foldLeft(x.wrap _) { (builder, wrapper) =>
      builder.compose(wrapper.wrap)
    }
  }
}
