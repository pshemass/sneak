package com.sneak.store.storm

import com.typesafe.config.Config
import kafka.consumer.{KafkaStream, Consumer, ConsumerConfig}
import com.sneak.store.util.ConfigurationLoader._
import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.message.ThriftMessageSerializer
import com.sneak.thrift.Message
import storm.scala.dsl.StormSpout

/**
 * Kafka Spout implementation
 */
class KafkaSpout(config: Config) extends StormSpout(
  outputFields = List("message"),
  isDistributed = true
) with Logging {


  val connector = {
    val kafkaSection: Config = config.getConfig("kafka")
    val kafkaConfig = new ConsumerConfig(kafkaSection)
    val connector = Consumer.create(kafkaConfig)


    connector
  }

  val stream: KafkaStream[Message, Message] = {
    val kafkaSection: Config = config.getConfig("kafka")
    val serializer = new ThriftMessageSerializer
    val topic = kafkaSection.getString("topic")
    connector.createMessageStreams(Map(topic -> 1), serializer, serializer).get(topic).get.head
  }

  val iterator = stream.iterator()


  def shutdown = {
    connector.shutdown()
  }


  def nextTuple(): Unit = {
    emit(iterator.next())
  }

}