package com.sneak.client.impl

import java.net.InetSocketAddress
import com.typesafe.scalalogging.slf4j.Logging
import kafka.producer.{KeyedMessage, ProducerConfig, Producer}
import com.sneak.thrift.Message
import java.util.Properties
import com.sneak.client.api.Settings

/**
  * Publishes events to Kafka topic.
  */
class KafkaEventPublisher(topic: String, zookeeper: Seq[InetSocketAddress]) extends EventPublisher with Logging {


   val producer: Producer[String, Message] = {
     val props = new Properties()
     props.put("zk.connect", zookeeper.toString)
     props.put("serializer.class", "com.sneak.client.com.sneak.client.impl.MessageSerializer")

     // Use random partitioner. Don't need the key type. Just set it to Integer.
     // The message is of type String.
     new Producer[String, Message](new ProducerConfig(props))
   }



   /**
    * Publish a metric. This should lead to delivering given metric object over the network
    * to a remote metrics store.
    * @param metric Published metric
    */
   def publish(metric: Message) {
     logger.debug(s"Sending message ${metric.name}")
     val msg = new KeyedMessage[String, Message](topic, metric)
     producer.send(msg)
   }
 }

object KafkaEventPublisher {
  import com.sneak.client.api.Converters._

  def apply(settings: Settings) = new KafkaEventPublisher(
    settings.topic,
    settings.kafkaZooKeeper
  )
}