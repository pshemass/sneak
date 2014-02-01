package com.sneak.store.store

import com.sneak.thrift.Message
import scala.concurrent.{Future, Promise}

/**
 * Stores metrics in a storage.
 *
 * User: fox
 * Date: 8/8/13
 * Time: 10:11 PM
 */
trait MetricsStore {

  /**
   * Persists the message in a persistent storage.
   *
   * @param metric
   */
  def storeMetric(metric: Message): Future[_]

  /**
   * Store metrics in batch manner.
   *
   * @param metrics Set of metrics
   * @return
   */
  def storeMetrics(metrics: Iterable[Message]): Future[_]
}
