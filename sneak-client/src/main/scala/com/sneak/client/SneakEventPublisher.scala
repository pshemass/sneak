package com.sneak.client

import org.joda.time.DateTime
import java.net.InetAddress
import com.sneak.thrift.Message

/**
 * The publishing interface defining actions related to publishing monitoring events.
 * Metrics should be published in asynchronuous manner. Therefore
 * there is no guarantee that metrics are delivered to the store.
 *
 * User: fox
 * Date: 7/13/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
trait SneakEventPublisher {

  /**
   * Publish a metric. This should lead to delivering given metric object over the network
   * to a remote metrics store.
   * @param metric Published metric
   */
  def publish(metric: Message)
}
