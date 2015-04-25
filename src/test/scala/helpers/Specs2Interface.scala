package helpers

import akka.http.scaladsl.testkit.TestFrameworkInterface
import org.specs2.execute.{Failure, FailureException}
import org.specs2.mutable.Specification
import org.specs2.specification.AfterAll

trait Specs2Interface extends TestFrameworkInterface with AfterAll {
  this: Specification ⇒

  def failTest(msg: String) = {
    val trace = new Exception().getStackTrace.toList
    val fixedTrace = trace.drop(trace.indexWhere(_.getClassName.startsWith("org.specs2")) - 1)
    throw new FailureException(Failure(msg, stackTrace = fixedTrace))
  }

  override def afterAll(): Unit = {
    cleanUp()
  }
}
