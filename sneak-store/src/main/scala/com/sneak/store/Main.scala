package com.sneak.store

import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.util.ConfigurationLoader
import akka.actor.ActorSystem
import com.sneak.store.core.WritingActor
import com.sneak.store.service.impl.{KafkaConsumer, CassandraMetricStore}

/**
 * Main application class.
 */
object Main extends App with Logging {

  val config = ConfigurationLoader.load()
  val store = CassandraMetricStore(config)
  val system = ActorSystem("sneak-system", config)

  val readActor = system.actorOf(WritingActor.props(store))
  val consumer = new KafkaConsumer(config)

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run() {
      consumer.shutdown
      system.shutdown()
      store.close()
    }
  })
}
