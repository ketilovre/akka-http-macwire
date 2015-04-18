package com.ketilovre.server.directives

import akka.http.model.StatusCodes._
import akka.http.model._
import akka.http.server.Directives._
import akka.http.server.RouteResult.{Complete, Rejected}
import akka.http.server._
import akka.http.server.directives.LoggingMagnet
import org.slf4j.LoggerFactory

object AccessLog {

  private val log = LoggerFactory.getLogger("access")

  private val translateRejection: Rejection => StatusCode = {
    case AuthorizationFailedRejection     => Forbidden
    case _: AuthenticationFailedRejection => Unauthorized
    case _                                => BadRequest
  }

  def apply(): Directive0 = {
    logRequestResult(LoggingMagnet(_ => logAccess(System.currentTimeMillis())))
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
