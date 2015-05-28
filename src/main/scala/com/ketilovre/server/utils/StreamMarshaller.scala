package com.ketilovre.server.utils

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.HttpCharsets._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{Flow, Source}
import akka.util.ByteString

/**
 * Helper utility to create ToResponseMarshallers for streaming responses.
 */
object StreamMarshaller {

  /**
   * Creates a custom streaming ToResponseMarshaller with a given media type, character set and
   * encoding flow.
   */
  def apply[A](mediaType: MediaType, charset: HttpCharset)
                   (encodingFlow: Flow[A, ByteString, Unit]): ToResponseMarshaller[Source[A, Unit]] = {
    Marshaller.withFixedCharset(mediaType, charset) { source =>
      HttpResponse(entity = HttpEntity.Chunked.fromData(mediaType, source.via(encodingFlow)))
    }
  }

  /** Shortcut functions for common datatype/charset combinations */
  def html[A](encodingFlow: Flow[A, ByteString, Unit]): ToResponseMarshaller[Source[A, Unit]] = {
    StreamMarshaller(`text/html`, `UTF-8`)(encodingFlow)
  }

  def json[A](encodingFlow: Flow[A, ByteString, Unit]): ToResponseMarshaller[Source[A, Unit]] = {
    StreamMarshaller(`application/json`, `UTF-8`)(encodingFlow)
  }

  def text[A](encodingFlow: Flow[A, ByteString, Unit]): ToResponseMarshaller[Source[A, Unit]] = {
    StreamMarshaller(`text/plain`, `UTF-8`)(encodingFlow)
  }

  def xml[A](encodingFlow: Flow[A, ByteString, Unit]): ToResponseMarshaller[Source[A, Unit]] = {
    StreamMarshaller(`application/xml`, `UTF-8`)(encodingFlow)
  }
}
