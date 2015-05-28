package com.ketilovre.server.routes

import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.GreetHandler
import com.ketilovre.server.utils.StreamMarshaller

import scala.concurrent.ExecutionContext

class GreetRoute(handler: GreetHandler)(implicit ec: ExecutionContext) extends PartialRoute {

  implicit val stringStreamMarshaller = StreamMarshaller.text {
    Flow[String].map(ByteString.fromString)
  }

  val route: Route = {
    get {
      path("greet" / Segment ~ PathEnd) { name =>
        complete(handler.hello(name))
      }
    }
  }
}
