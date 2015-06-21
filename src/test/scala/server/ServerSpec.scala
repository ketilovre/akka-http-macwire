package server

import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Sink, Source}
import com.ketilovre.config.{ServerHostConfig, ServerPortConfig}
import com.ketilovre.server.routes.PartialRoute
import com.ketilovre.server.{Api, Server}
import com.softwaremill.macwire.Tagging._
import helpers.{AkkaSpec, BaseSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ServerSpec extends BaseSpec with AkkaSpec {

  val host = "localhost".taggedWith[ServerHostConfig]

  val port = 9999.taggedWith[ServerPortConfig]

  val dummyApi = new PartialRoute {
    def route: Route = complete("foo")
  }

  def sendRequest(req: HttpRequest): Future[HttpResponse] = {
    Source.single(req).via(
      Http().outgoingConnection(host, port)
    ).runWith(Sink.head)
  }

  "Server" should {

    "start an http server on a given host and port" in {

      val server = new Server(new Api(Seq(dummyApi), Seq.empty), host, port)

      val connection = Await.result(server.bind, 2.seconds)

      val result = sendRequest(Get(s"http://$host:$port")).map { res =>
        res.status mustEqual StatusCodes.OK
        Unmarshal(res.entity).to[String].map(_ mustEqual "foo").await
      }.await

      Await.ready(connection.unbind(), 2.seconds)

      result
    }
  }
}
