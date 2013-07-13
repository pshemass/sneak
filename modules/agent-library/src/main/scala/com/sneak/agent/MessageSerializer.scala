package com.sneak.agent

/**
 * Serializes messages
 *
 * User: fox
 * Date: 7/13/13
 * Time: 11:54 PM
 */
trait MessageSerializer[TYPE] {

  def serialize(msg: TYPE): Array[Byte]
}
