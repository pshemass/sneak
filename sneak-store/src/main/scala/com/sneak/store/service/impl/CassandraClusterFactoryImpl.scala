package com.sneak.store.service.impl

import com.datastax.driver.core.{ProtocolOptions, Cluster}
import com.sneak.store.util.Configuration
import com.twitter.logging.Logger
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 10/13/13
 * Time: 8:14 PM
 */
class CassandraClusterFactoryImpl(config: Configuration)
  extends CassandraClusterFactory with Logging {

  val CASSANDRA_SEEDS: String = "cassandra.hosts"

  val CASSANDRA_PORT: String = "cassandra.port"

  val port = config.getString(CASSANDRA_PORT).toInt

  val hosts = config.getString(CASSANDRA_SEEDS).split(",")

  lazy val connect: Cluster = {
    Cluster.builder().
      addContactPoints(hosts: _*).
      withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(port).
      build()
  }


}
