package com.ketilovre.server

import akka.http.server.Route

trait Wrapper {

  def wrap(route: Route): Route

}
