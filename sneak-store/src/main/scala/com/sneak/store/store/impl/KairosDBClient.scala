package com.sneak.store.store.impl

import com.sneak.store.store.MetricsStore
import com.sneak.thrift.Message
import scala.concurrent.Future
import com.datastax.driver.core.ResultSet
import com.typesafe.config.Config

/**
 * Client to KairosDB database.
 */
class KairosDBClient(implicit config: Config) extends MetricsStore {

  /**
   * Persists the message in a persistent storage.
   *
   * @param metric
   */
  override def storeMetric(metric: Message): Future[ResultSet] = ???

  /**
   * Store metrics in batch manner.
   *
   * @param metrics Set of metrics
   * @return
   */
  override def storeMetrics(metrics: Iterable[Message]): Future[Unit] = ???
}

object KairosDBClient {
  def open( implicit config: Config): KairosDBClient = {
    new KairosDBClient
  }
}
