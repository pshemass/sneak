package com.sneak.store

import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.service.impl.{CassandraClusterFactoryImpl, CassandraClusterFactory, CassandraMetricStore}
import com.sneak.store.util.FileConfiguration
import akka.actor.{Props, ActorSystem}
import com.sneak.store.core.WritingActor

/**
 * Created by fox on 10/14/13.
 */
class Main extends App with Logging {

  val config = FileConfiguration()

  val clusterFactory = new CassandraClusterFactoryImpl(config)

  val store = new CassandraMetricStore(config, clusterFactory)

  val system = ActorSystem()

  val readActor = system.actorOf(WritingActor.props(store))


}
