package server.handlers

import com.ketilovre.server.handlers.HelloHandler
import helpers.BaseSpec

class HelloHandlerSpec extends BaseSpec {

  val handler = new HelloHandler()

  "hello" should {

    "greet the user based on the input" ! prop { str: String =>
      handler.hello(str) mustEqual s"Hello $str!"
    }
  }
}
