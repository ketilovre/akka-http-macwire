package com.ketilovre.server.wrappers

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.logRequestResult
import akka.http.scaladsl.server.RouteResult.{Complete, Rejected}
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.LoggingMagnet
import com.ketilovre.server.Wrapper
import org.slf4j.LoggerFactory

class AccessLog extends Wrapper {

  private val log = LoggerFactory.getLogger("access")

  private val translateRejection: Rejection => StatusCode = {
    case AuthorizationFailedRejection     => Forbidden
    case _: AuthenticationFailedRejection => Unauthorized
    case _                                => BadRequest
  }

  def wrap(route: Route): Route = {
    logRequestResult(LoggingMagnet(_ => logAccess(System.currentTimeMillis()))) {
      route
    }
  }

  private def logAccess(start: Long)(req: HttpRequest): RouteResult => Unit = {
    case Complete(response) => log.info(completion(start, req, response))
    case Rejected(rejections) => log.info {
      rejection(start, req) {
        RejectionHandler.applyTransformations(rejections)
      }
    }
  }

  private def completion(start: Long, req: HttpRequest, res: HttpResponse): String = {
    message(start, res.status, req.method, req.uri)
  }

  private def rejection(start: Long, req: HttpRequest): Seq[Rejection] => String = {
    case Nil     => message(start, NotFound, req.method, req.uri)
    case r :: rs => message(start, translateRejection(r), req.method, req.uri)
  }

  private def message(start: Long, status: StatusCode, method: HttpMethod, uri: Uri): String = {
    val duration = System.currentTimeMillis() - start
    s""" "${method.name} ${uri.toRelative}" ${status.intValue()} ${duration}ms """.trim
  }
}
