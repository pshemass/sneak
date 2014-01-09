package com.sneak.client.api

import org.specs2.mutable.Specification
import java.net.InetSocketAddress
import org.specs2.matcher.Matchers

/**
 * Created by fox on 09/01/14.
 */
class ConvertersSpec extends Specification with Matchers {
  "Converters" should {
    "convert string to sequence of single InetSocketAddress" in {
      Converters.stringToInetSocketAddress("host:123") must_== Seq(new InetSocketAddress("host",123))
    }

    "convert string to sequence of multiple InetSocketAddress" in {
      Converters.stringToInetSocketAddress("host1:1,host2:2") must_== Seq(
        new InetSocketAddress("host1",1),
        new InetSocketAddress("host2",2)
      )
    }
  }
}
