package com.ketilovre.config

import com.softwaremill.macwire.Tagging._
import com.typesafe.config.ConfigFactory

trait ConfigModule {

  private val config = ConfigFactory.load()

  lazy val serverHostConfig: String @@ ServerHostConfig = {
    config.getString("server.host").taggedWith[ServerHostConfig]
  }

  lazy val serverPortConfig: Int @@ ServerPortConfig = {
    config.getInt("server.port").taggedWith[ServerPortConfig]
  }
}
