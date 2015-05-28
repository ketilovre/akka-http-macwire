package com.ketilovre.server.handlers

import akka.stream.scaladsl.Source

import scala.util.Random

class StreamingHandler {

  private val numSegments = 100000

  private val segmentLength = 100

  /** Generates ~10MB of gibberish */
  def alphaNumericStream: Source[String, Unit] = Source { () =>
    Iterator.fill(numSegments)(Random.alphanumeric.take(segmentLength).mkString)
  }
}
