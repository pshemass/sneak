package com.sneak.client

/**
 * Created with IntelliJ IDEA.
 * User: rzbiq
 * Date: 7/17/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
class KafkaSneakEventPublisher extends SneakEventPublisher{
  /**
   * Publish a metric. This should lead to delivering given metric object over the network
   * to a remote metrics store.
   * @param metric Published metric
   */
  def publish(metric: Metric) {}
}
