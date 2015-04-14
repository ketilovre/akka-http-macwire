package com.ketilovre.server

import akka.http.model.HttpRequest
import akka.http.server.directives.LoggingMagnet
import akka.http.server.{Directives, Route}
import com.ketilovre.server.routes.PartialRoute
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

class Api(routes: Seq[PartialRoute])(implicit ec: ExecutionContext) extends Directives {

  private val accessLogger = LoggerFactory.getLogger("access")

  lazy val route: Route = {
    logRequest(LoggingMagnet(_ => logAccess)) {
      encodeResponse {
        concatenatedRoutes
      }
    }
  }

  private val concatenatedRoutes: Route = routes match {
    case Nil      => complete("Please add some routes")
    case x :: Nil => x.route
    case x :: xs  => xs.foldLeft(x.route) { (builder, partial) =>
      builder ~ partial.route
    }
  }

  def logAccess(req: HttpRequest): Unit = {
    accessLogger.info(s"method=${req.method.name} uri=${req.uri.toString()} headers=${req.headers.toString()}")
  }
}
