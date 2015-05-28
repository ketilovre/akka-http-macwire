package server.wrappers

import akka.http.scaladsl.coding.{Deflate, Encoder, Gzip, NoCoding}
import akka.http.scaladsl.model.headers.HttpEncodings._
import akka.http.scaladsl.model.headers.{`Accept-Encoding`, `Content-Encoding`}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.ketilovre.server.wrappers.Encoding
import helpers.{BaseSpec, RouteSpec}

class EncodingSpec extends BaseSpec with RouteSpec {

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

    "not alter unencoded requests" in prop { str: String =>

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

    "not encode responses if not requested" in prop { str: String =>
      Post("/", str) ~> `Accept-Encoding`(identity) ~> route ~> check {
        entityAs[String] mustEqual str
      }
    }
  }
}
