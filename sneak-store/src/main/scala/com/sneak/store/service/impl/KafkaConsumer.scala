package com.sneak.store.service.impl

import com.typesafe.config.Config
import kafka.consumer.{Consumer, ConsumerConfig}
import com.sneak.store.util.ConfigurationLoader._

/**
 * Consumes messages from Kafka topic.
 */
class KafkaConsumer(config: Config) {


  def createConsumer = {
    val kafkaSection: Config = config.getConfig("kafka")
    val kafkaConfig = new ConsumerConfig(kafkaSection)
    val connector = Consumer.create(kafkaConfig)

    //TODO: finish
  }

  def shutdown = ???


}

object KafkaConsumer {


}
