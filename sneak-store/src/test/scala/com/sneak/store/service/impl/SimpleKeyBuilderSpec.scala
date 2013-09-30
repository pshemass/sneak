package com.sneak.store.service.impl

import org.specs2.mock._
import org.specs2.Specification
import com.sneak.thrift.Message

/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 9/30/13
 * Time: 9:39 PM
 */
class SimpleKeyBuilderSpec extends Specification { def is = s2"""
  This specification checks the key builder creates correct keys
    ${skb.buildKey(msg) must_==("test.local.1")}
                                                              """

  val skb = new SimpleKeyBuilder

  val msg = Message(1l, "test", 1, "local", "testap")

}
