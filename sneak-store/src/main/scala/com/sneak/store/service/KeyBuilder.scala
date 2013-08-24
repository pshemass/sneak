package com.sneak.store.service

import com.sneak.thrift.Message

/**
 * Builds key for given metric.
 *
 * User: fox
 * Date: 8/24/13
 * Time: 11:41 PM
 */
trait KeyBuilder {

  /**
   * Creates meaningful metric key
   *
   * @param message Metric to build the key for
   * @return Returns the key
   */
  def buildKey(message: Message): String
}
