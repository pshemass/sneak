package com.sneak.store.service.impl

import com.typesafe.config.Config
import kafka.consumer.{KafkaStream, Consumer, ConsumerConfig}
import com.sneak.store.util.ConfigurationLoader._
import kafka.message.MessageAndMetadata
import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.message.ThriftMessageSerializer
import com.sneak.thrift.Message

/**
 * Consumes messages from Kafka topic.
 */
class KafkaConsumer(config: Config) extends Logging {


  val connector = {
    val kafkaSection: Config = config.getConfig("kafka")
    val kafkaConfig = new ConsumerConfig(kafkaSection)
    val connector = Consumer.create(kafkaConfig)
    val topic = kafkaSection.getString("topic")
    val serializer = new ThriftMessageSerializer
    val stream: KafkaStream[Message, Message] =
      connector.createMessageStreams(Map(topic -> 1), serializer, serializer).get(topic).get.head

    try {
      for(message <- stream) {
        try {
          processMessage(message)
        } catch {
          case e =>
            logger.error("error processing message, skipping and resume consumption: " + e)
        }
      }
    } catch {
      case e => logger.error("error processing message, stop consuming: " + e)
    }
    connector
  }

  def processMessage(metadata: MessageAndMetadata[Message, Message]) {

  }

    //TODO: finish


  def shutdown = {
    connector.shutdown()
  }


}