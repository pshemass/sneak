package com.sneak.client.api

import com.sneak.client.impl.{KafkaEventPublisher, EventPublisher}
import com.typesafe.scalalogging.slf4j.Logging

/**
 * Entry point class initiating monitoring connections and API.
 */
class MonitoringSystem(implicit publisher: EventPublisher, settings: Settings) extends Logging {

  def metricCaptor(host: String, applicationName: String): MetricsCaptor = {
    logger.warn("Creating new metrics captor")
    new MetricsCaptor(host, applicationName)
  }

  def shutdown() {
    logger.info("Shutting down Sneak metrics")
    publisher.shutdown()
  }

}

object MonitoringSystem {

  def apply(implicit settings: Settings) = {
    implicit val publisher = KafkaEventPublisher(settings)
    new MonitoringSystem()
  }

}
