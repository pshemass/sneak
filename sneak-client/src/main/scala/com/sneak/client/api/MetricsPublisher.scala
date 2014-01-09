package com.sneak.client.api

import com.sneak.thrift.Message
import scala.compat.Platform
import com.sneak.client.impl.EventPublisher


/**
 * Main API class responsible for communicating metrics events to store. The implementation buffers the metrics
 * and publishes them in an asynchronous manner.
 */
class MetricsPublisher(publisher: EventPublisher, host: String, application: String) {

  def capture(name: String, value: Double): Unit = {
    capture(name, value, Map.empty)
  }

  def capture(name: String, value: Double, attributes: Map[String, String]): Unit = {
    val message = Message(
      Platform.currentTime,
      name,
      value,
      host,
      application,
      attributes
    )
    publisher.publish(message)
  }
}

