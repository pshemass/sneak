package com.sneak.store.service

import com.sneak.thrift.Message
import com.datastax.driver.core.ResultSet
import scala.concurrent.Future

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
  def storeMetric(metric: Message): Future[ResultSet]
}
