package com.sneak.store

import com.sneak.thrift.Message
import com.twitter.cassie.{Column, Cluster}
import com.twitter.cassie.codecs._
import com.typesafe.scalalogging.slf4j.Logging


/**
 * Store that persists messages to Cassandra database.
 *
 * User: fox
 * Date: 8/8/13
 * Time: 10:52 PM
 */
trait CassandraMetricStore extends MetricsStore with Logging {

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
  val cluster: Cluster

  val keySpace = {
    cluster.keyspace(KeySpaceName).connect()
  }

  val columnFamily = {
    keySpace.columnFamily( ColumnFamily, Utf8Codec, Utf8Codec, Utf8Codec)
  }


  /**
   * Creates meaningful metric key
   *
   * @param message Metric to build the key for
   * @return Returns the key
   */
  def buildKey(message: Message): String

  def storeMetric(metric: Message) {
    logger.info(s"Storing message ${metric.name}")
    columnFamily.insert(buildKey(metric), Column("name", metric.name))
  }

}
