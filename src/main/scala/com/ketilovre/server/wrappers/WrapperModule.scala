package com.ketilovre.server.wrappers

import com.ketilovre.server.Wrapper
import com.softwaremill.macwire.wire

trait WrapperModule {

  lazy val wrappers: Seq[Wrapper] = Seq(
    accessLogWrapper,
    encodingWrapper,
    corsWrapper,
    securityWrapper
  )

  lazy val accessLogWrapper: AccessLog = wire[AccessLog]

  lazy val encodingWrapper: Encoding = wire[Encoding]

  lazy val corsWrapper: CORSHeaders = wire[CORSHeaders]

  lazy val securityWrapper: SecurityHeaders = wire[SecurityHeaders]
}
