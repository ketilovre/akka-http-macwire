package com.ketilovre.server.wrappers

import akka.http.scaladsl.server.Route
import com.softwaremill.macwire.wire

trait Wrapper {
  def wrap(route: Route): Route
}

trait WrapperModule {

  lazy val wrappers: Seq[Wrapper] = Seq(
    wire[AccessLog],
    wire[Encoding],
    wire[CorsHeaders],
    wire[SecurityHeaders]
  )
}
