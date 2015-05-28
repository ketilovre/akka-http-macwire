package com.ketilovre.server.wrappers

import com.ketilovre.server.Wrapper
import com.softwaremill.macwire.wire

trait WrapperModule {

  lazy val wrappers: Seq[Wrapper] = Seq(
    wire[AccessLog],
    wire[Encoding],
    wire[CorsHeaders],
    wire[SecurityHeaders]
  )
}
