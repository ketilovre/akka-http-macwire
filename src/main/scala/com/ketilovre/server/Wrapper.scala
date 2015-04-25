package com.ketilovre.server

import akka.http.scaladsl.server.Route

trait Wrapper {

  def wrap(route: Route): Route

}
