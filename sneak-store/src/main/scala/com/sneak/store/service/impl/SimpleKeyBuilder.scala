package com.sneak.store.service.impl

import com.sneak.thrift.Message
import com.sneak.store.service.KeyBuilder

/**
 * Builds key for given metric by concatenating name of the message, source host and timestamp.
 * User: fox
 * Date: 8/24/13
 * Time: 11:42 PM
 */
class SimpleKeyBuilder extends KeyBuilder{

  /**
   * Creates meaningful metric key
   *
   * @param message Metric to build the key for
   * @return Returns the key
   */
  def buildKey(message: Message): String = {
    s"${message.name}.${message.host}.${message.timestamp}"
  }
}
