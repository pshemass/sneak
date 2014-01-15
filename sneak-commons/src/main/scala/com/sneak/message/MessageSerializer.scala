package com.sneak.message

import kafka.serializer.{Decoder, Encoder}
import com.sneak.thrift.Message

/**
 * Serializes messages
 *
 * User: fox
 * Date: 7/13/13
 * Time: 11:54 PM
 */
trait MessageSerializer extends Encoder[Message] with Decoder[Message] {

  def toBytes(p1: Message): Array[Byte]

  def fromBytes(p1: Array[Byte]): Message
}
