package com.sneak.client

/**
 * Broker is responsible for sending serialized messages to metrics store.
 *
 * User: fox
 * Date: 7/13/13
 * Time: 11:52 PM
 */
trait MessageBroker {

  /**
   * Send a serialized message
   * @param message
   */
  def send(message: Array[Byte])

}
