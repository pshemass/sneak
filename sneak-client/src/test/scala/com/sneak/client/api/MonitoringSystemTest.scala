package com.sneak.client.api

import org.specs2.mutable.Specification
import com.sneak.client.impl.KafkaEventPublisher._
import com.sneak.client.impl.{EventPublisher, KafkaEventPublisher}
import com.sneak.thrift.Message
import org.specs2.mock.Mockito

class MonitoringSystemTest extends Specification with Mockito {

  "Monitoring system" should {
    "initiate" in {
      val settings = Settings("localhost:666", "sneak")
      val publisher = mock[EventPublisher]
      val system = MonitoringSystem(settings, publisher)
      val captor = system.metricCaptor("test", "testApp")
      captor.capture("test.metric", 666)

      there was one(publisher).publish(any[Message])
    }
  }
}

