package com.sneak.store

import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.service.impl.CassandraMetricStore
import com.sneak.store.util.FileConfiguration
import akka.actor.ActorSystem
import com.sneak.store.core.WritingActor

/**
 * Main application class.
 */
class Main extends App with Logging {

  val config = FileConfiguration()

  val store = CassandraMetricStore(config)

  val system = ActorSystem()

  val readActor = system.actorOf(WritingActor.props(store))


}
