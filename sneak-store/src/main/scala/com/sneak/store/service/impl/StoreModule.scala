package com.sneak.store.service.impl

import com.sneak.store.util.FileConfiguration
import java.io.File

/**
 * Service initiator and locator.
 * User: fox
 * Date: 9/2/13
 * Time: 11:20 PM
 */
trait StoreModule {

  import com.softwaremill.macwire.MacwireMacros._

  val file = new File("store.properties")

  lazy val configuration = wire[FileConfiguration]
  lazy val keyBuilder = wire[SimpleKeyBuilder]
  lazy val metricStore = wire[CassandraMetricStore]
}
