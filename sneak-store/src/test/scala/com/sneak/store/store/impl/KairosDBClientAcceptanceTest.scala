package com.sneak.store.store.impl

import org.specs2.mutable.Specification
import com.typesafe.config.{ConfigFactory, Config}

class KairosDBClientAcceptanceTest extends Specification {

  val config = ConfigFactory.parseString(
    """
      |kairosdb {
      |  rest {
      |    host = 127.0.0.1
      |    port = 8080
      |  }
      |  timeout = 10
      |}
    """.stripMargin)

  "client " should {
    "store a value" in {
      val client = KairosDBClient.open(config)

      //TODO finish
      val test = ""
      test mustEqual  ""
    }
  }
}
