package com.ketilovre.server

import akka.actor.ActorSystem
import akka.http.model.headers.CacheDirectives.`max-age`
import akka.http.model.headers.`Cache-Control`
import akka.http.model.{HttpRequest, HttpResponse}
import akka.http.server.{Directives, Route, RoutingSettings, RoutingSetup}
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Flow
import com.ketilovre.server.directives.AccessLog

import scala.concurrent.ExecutionContext

class Api(partialRoutes: Seq[PartialRoute])
         (implicit sys: ActorSystem, mat: ActorFlowMaterializer, ec: ExecutionContext) extends Directives {

  implicit private val routingSettings = RoutingSettings.default

  implicit private val routingSetup: RoutingSetup = RoutingSetup.apply

  lazy val routeFlow: Flow[HttpRequest, HttpResponse, Unit] = Route.handlerFlow(route)

  lazy val route: Route = {
    AccessLog() {
      globalHeaders {
        encoding {
          concatenatedRoutes
        }
      }
    }
  }

  private def globalHeaders(route: Route): Route = {
    respondWithHeader(`Cache-Control`(`max-age`(1))) {
      route
    }
  }

  private def encoding(route: Route): Route = {
    decodeRequest {
      encodeResponse {
        route
      }
    }
  }

  private val concatenatedRoutes: Route = partialRoutes match {
    case Nil     => complete("Please add some routes")
    case x :: xs => xs.foldLeft(x.route) { (builder, partial) =>
      builder ~ partial.route
    }
  }
}
