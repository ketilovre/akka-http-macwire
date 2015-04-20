package server

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.client.RequestBuilding.Get
import akka.http.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.http.server.Route
import akka.http.unmarshalling.Unmarshal
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.ketilovre.config.{ServerHostConfig, ServerPortConfig}
import com.ketilovre.server.{Api, PartialRoute, Server}
import com.softwaremill.macwire.Tagging._
import helpers.BaseSpec
import org.specs2.specification.AfterAll

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ServerSpec extends BaseSpec with AfterAll {

  implicit val ac = ActorSystem()

  implicit val afm = ActorFlowMaterializer()

  import ac.dispatcher

  val host = "localhost".taggedWith[ServerHostConfig]

  val port = 8888.taggedWith[ServerPortConfig]

  val dummyApi = new PartialRoute {
    def route: Route = complete("foo")
  }

  def sendRequest(req: HttpRequest): Future[HttpResponse] = {
    Source.single(req).via(
      Http().outgoingConnection(host, port)
    ).runWith(Sink.head)
  }

  def afterAll(): Unit = {
    ac.shutdown()
  }

  "boot" should {

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
