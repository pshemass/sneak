package com.sneak.store.service

import com.sneak.store.service.impl.CassandraMetricsStoreService
import com.sneak.store.util.{Configuration, FileConfiguration}

/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 9/2/13
 * Time: 11:20 PM
 */
class Bootstrap {

  val configuration = FileConfiguration("sneak.properties")

  val metricStore = new CassandraMetricsStoreService {
    val config: Configuration = configuration
  }
}
