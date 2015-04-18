package helpers

import akka.http.testkit.RouteTest
import org.scalacheck.{Arbitrary, Gen}
import org.specs2.ScalaCheck
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

trait BaseSpec extends Specification with Mockito with ScalaCheck

trait RouteSpec extends BaseSpec with Specs2Interface with RouteTest {

  implicit val urlSafeStringGen: Arbitrary[String] = Arbitrary(Gen.alphaStr)
}
