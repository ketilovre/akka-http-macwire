package server.handlers

import com.ketilovre.server.handlers.GreetHandler
import helpers.BaseSpec

class GreetHandlerSpec extends BaseSpec {

  val handler = new GreetHandler()

  "HelloHandler" should {

    "hello" should {

      "greet the user based on the input" ! prop { str: String =>
        handler.hello(str) mustEqual s"Hello $str!"
      }
    }
  }
}
