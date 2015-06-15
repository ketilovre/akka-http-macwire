package com.ketilovre.server.wrappers

import com.ketilovre.server.Wrapper

trait WrapperModule {

  lazy val wrappers: Seq[Wrapper] = Seq.empty

}
