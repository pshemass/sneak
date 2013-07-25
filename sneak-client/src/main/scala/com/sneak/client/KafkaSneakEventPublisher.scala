package com.sneak.client

import kafka.producer.{KeyedMessage, ProducerConfig, Producer}
import java.util.Properties
import org.slf4j.LoggerFactory
import com.sneak.thrift.Message


/**
 * Created with IntelliJ IDEA.
 * User: rzbiq
 * Date: 7/17/13
 * Time: 11:27 PM
 */
trait KafkaSneakEventPublisher extends SneakEventPublisher {

  val topic: String

  val producer = {
    val props = new Properties()
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    props.put("zk.connect", "localhost:2181")
    props.put("serializer.class", "com.sneak.client.MessageSerializer")

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
    val msg = new KeyedMessage[String, Message](topic, metric)
    producer.send(Seq(msg))
  }
}
