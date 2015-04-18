package com.ketilovre.server

import akka.http.server.{Directives, Route}

trait PartialRoute extends Directives {

  def route: Route

}
