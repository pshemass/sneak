package com.sneak.store.service.impl

import com.datastax.driver.core.Cluster

/**
 * Creates cluster connection
 * User: fox
 * Date: 10/13/13
 * Time: 7:58 PM
 */
trait CassandraClusterFactory {
  def connect: Cluster
}
