package com.sneak.client.impl

import com.sneak.thrift.Message
import org.specs2.mutable.Specification

class ThriftMessageSerializerTest extends Specification  {

  "ThriftMessageSerializer serialization and deserialization" should {
    "be associative" in {
      val serializer = new ThriftMessageSerializer
      val msg = Message(System.currentTimeMillis(), "test", 1.0, "localhost", "unit-test")
      val bytes = serializer.toBytes(msg)
      val clone = serializer.fromBytes(bytes)
      clone mustEqual msg
    }
  }

}
