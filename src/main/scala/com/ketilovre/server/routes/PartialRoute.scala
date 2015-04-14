package com.ketilovre.server.routes

import akka.http.server.{Directives, Route}

trait PartialRoute extends Directives {

  def route: Route

}
