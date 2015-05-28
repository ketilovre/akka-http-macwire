package server.utils

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpCharsets, HttpRequest, MediaTypes}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.ByteString
import com.ketilovre.server.utils.StreamMarshaller
import helpers.{AkkaSpec, BaseSpec}

class StreamMarshallerSpec extends BaseSpec with AkkaSpec {

  "StreamMarshaller" should {

    implicit val stringStreamMarshaller = {
      StreamMarshaller(MediaTypes.`text/plain`, HttpCharsets.`UTF-8`) {
        Flow[String].map(ByteString.fromString)
      }
    }

    "marshall arbitrary sources to bytestrings" ! prop { list: List[String] =>
      list.nonEmpty ==> {
        Marshal(Source(list)).toResponseFor(HttpRequest()).flatMap { response =>
          response.entity.dataBytes.runWith(Sink.fold(ByteString.empty)(_ ++ _)).map { bytes =>
            bytes.utf8String mustEqual list.mkString
          }
        }.await
      }
    }
  }
}
