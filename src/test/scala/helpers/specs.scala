package helpers

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.RouteTest
import akka.stream.ActorMaterializer
import org.scalacheck.{Arbitrary, Gen}
import org.specs2.ScalaCheck
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

trait BaseSpec extends Specification with Mockito with ScalaCheck

trait AkkaSpec extends AfterAll {
  self: BaseSpec =>

  implicit val system = ActorSystem("test")

  implicit val dispatcher = system.dispatcher

  implicit val materializer = ActorMaterializer()

  def afterAll(): Unit = {
    system.shutdown()
  }
}

trait RouteSpec extends Specs2Interface with RouteTest {
  self: BaseSpec =>

  implicit val urlSafeStringGen: Arbitrary[String] = Arbitrary(Gen.alphaStr)
}
