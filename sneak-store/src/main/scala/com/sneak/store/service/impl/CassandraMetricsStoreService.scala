package com.sneak.store.service.impl

import com.twitter.cassie.Cluster
import com.sneak.store.service.{CassandraMetricStore}
import com.sneak.store.util.Configuration

/**
 * Service object initiating and maintaining cassandra connections.
 *
 * User: fox
 * Date: 8/24/13
 * Time: 11:39 PM
 */
trait CassandraMetricsStoreService extends CassandraMetricStore {

  val config: Configuration

  val cluster = new Cluster(config getString("cassandra.hosts"))

  val keyBuilder = new SimpleKeyBuilder
}
