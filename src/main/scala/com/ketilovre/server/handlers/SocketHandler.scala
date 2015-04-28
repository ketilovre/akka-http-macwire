package com.ketilovre.server.handlers

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.Flow

class SocketHandler {

  val hello: Flow[Message, Message, Unit] = Flow[Message].map {
    case TextMessage.Strict(name) => TextMessage.Strict(s"Hello $name!")
    case _                        => TextMessage.Strict("Unsupported message type")
  }
}
