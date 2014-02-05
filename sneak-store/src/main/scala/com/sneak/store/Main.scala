package com.sneak.store

import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.util.ConfigurationLoader
import akka.actor.ActorSystem
import com.sneak.store.actors.WritingActor
import com.sneak.store.store.impl.KairosDBClient
import scala.concurrent.duration._

/**
 * Main application class.
 */
object Main extends App with Logging {

  implicit val config = ConfigurationLoader.load()
  val system = ActorSystem("sneak-system", config)

  val store = KairosDBClient.open

  val readActor = system.actorOf(WritingActor.props(store, 100 millis, 100 millis))

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run() {
      system.shutdown()
    }
  })
}
