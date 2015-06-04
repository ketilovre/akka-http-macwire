package com.ketilovre.server.routes

import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import com.ketilovre.server.PartialRoute
import com.ketilovre.server.handlers.StreamingHandler
import com.ketilovre.server.utils.StreamMarshaller

class StreamingRoute(handler: StreamingHandler) extends PartialRoute {

  implicit val stringStreamMarshaller = StreamMarshaller.text {
    Flow[String].map(ByteString.fromString)
  }

  def route: Route = {
    get {
      path("random") {
        complete(handler.alphaNumericStream)
      }
    }
  }
}
