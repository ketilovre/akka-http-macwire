package server.wrappers

import akka.http.coding.{Deflate, Encoder, Gzip, NoCoding}
import akka.http.model.headers.HttpEncodings._
import akka.http.model.headers.{`Accept-Encoding`, `Content-Encoding`}
import akka.http.server.Directives._
import akka.http.server.Route
import akka.util.ByteString
import com.ketilovre.server.wrappers.Encoding
import helpers.RouteSpec


class EncodingSpec extends RouteSpec {

  val encoding = new Encoding()

  val route: Route = encoding.wrap {
    entity(as[String]) { msg =>
      complete(msg)
    }
  }

  def compress(input: String, encoder: Encoder): ByteString = {
    val compressor = encoder.newCompressor
    compressor.compressAndFlush(ByteString(input)) ++ compressor.finish()
  }

  "decoding" should {

    "unzip requests" in prop { str: String =>

      Post("/", compress(str, Gzip)) ~> `Content-Encoding`(gzip) ~> `Accept-Encoding`(identity) ~>
        route ~> check {
          entityAs[String] mustEqual str
        }
    }

    "inflate requests" in prop { str: String =>

      Post("/", compress(str, Deflate)) ~> `Content-Encoding`(deflate) ~> `Accept-Encoding`(identity) ~>
        route ~> check {
          entityAs[String] mustEqual str
        }
    }

    "leave identity alone" in prop { str: String =>

      Post("/", compress(str, NoCoding)) ~> `Content-Encoding`(identity) ~> `Accept-Encoding`(identity) ~>
        route ~> check {
          entityAs[String] mustEqual str
        }
    }
  }

  "encoding" should {

    "gzip responses" in prop { str: String =>

      Post("/", str) ~> `Accept-Encoding`(gzip) ~> route ~> check {
        entityAs[String] mustEqual compress(str, Gzip).decodeString("UTF-8")
      }
    }

    "deflate responses" in prop { str: String =>
      Post("/", str) ~> `Accept-Encoding`(deflate) ~> route ~> check {
        entityAs[String] mustEqual compress(str, Deflate).decodeString("UTF-8")
      }
    }

    "leave identity alone" in prop { str: String =>
      Post("/", str) ~> `Accept-Encoding`(identity) ~> route ~> check {
        entityAs[String] mustEqual str
      }
    }
  }
}
