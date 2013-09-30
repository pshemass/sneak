package com.sneak.store.service.impl

import com.sneak.thrift.Message
import com.twitter.cassie.{Column, Cluster}
import com.twitter.cassie.codecs._
import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.util.Configuration
import com.sneak.store.service.{MetricsStore, KeyBuilder}


/**
 * Store that persists messages to Cassandra database.
 *
 * User: fox
 * Date: 8/8/13
 * Time: 10:52 PM
 */
class CassandraMetricStore(keyBuilder: KeyBuilder, config: Configuration) extends MetricsStore with Logging {

  /**
   * Key space for storing metrics
   */
  val KeySpaceName = "metrics"

  /**
   * Column family for storing metrics
   */
  val ColumnFamily = "metrics"

  /**
    * Cassandra cluster connection
   */
  val cluster: Cluster = new Cluster(config getString("cassandra.hosts"), null)

  val keySpace = {
    cluster.keyspace(KeySpaceName).connect()
  }

  val columnFamily = {
    keySpace.columnFamily( ColumnFamily, Utf8Codec, Utf8Codec, Utf8Codec)
  }



  def storeMetric(metric: Message) {
    logger.info(s"Storing message ${metric.name}")
    columnFamily.insert(keyBuilder buildKey(metric), Column("name", metric.name))
  }

}
