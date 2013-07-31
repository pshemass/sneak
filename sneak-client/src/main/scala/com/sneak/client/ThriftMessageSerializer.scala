package com.sneak.client

import com.sneak.thrift.Message
import org.apache.thrift.TSerializer
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TIOStreamTransport
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

/**
 * Implementation that serializes Message object to and from byte array.
 * User: fox
 * Date: 7/30/13
 * Time: 10:23 PM
 */
class ThriftMessageSerializer extends MessageSerializer {

  def toBytes(msg: Message): Array[Byte] = {
    val os = new ByteArrayOutputStream()
    val thriftTransport = new TIOStreamTransport(os)
    val protocol = new TBinaryProtocol(thriftTransport)
    msg.write(protocol)
    return os.toByteArray
  }

  def fromBytes(bytes: Array[Byte]): Message = {
    val is = new ByteArrayInputStream(bytes)
    val transport = new TIOStreamTransport(is)
    val protocol = new TBinaryProtocol(transport)
    Message(protocol)
  }
}
