package com.sneak.store.actors

import akka.actor.{Props, Actor}
import com.sneak.store.store.MetricsStore
import com.sneak.thrift.Message
import com.typesafe.scalalogging.slf4j.Logging
import WritingActor._
import scala.concurrent.duration.FiniteDuration


object WritingActor {

  sealed trait OperationReply {
    def id: Long
  }

  /**
   * Reply message
   * @param id Id of message written.
   */
  case class Saved(id: Long) extends OperationReply

  def props(store: MetricsStore, singleTimeout: FiniteDuration, multiTimeout: FiniteDuration) =
    Props(classOf[WritingActor], store, singleTimeout, multiTimeout)

}

/**
 * Write messages to storage. The actor extends GenericBuncher trait to batch event updates.
 */
class WritingActor(store: MetricsStore, singleTimeout: FiniteDuration, multiTimeout: FiniteDuration)
  extends GenericBuncher[Message, List[Message]](singleTimeout, multiTimeout)
          with Logging {

  def saveMessage(message: Message) {
    logger.info("Storing message {}", message.name)
    store.storeMetric(message)
  }

  override protected def empty: List[Message] = Nil

  override protected def send(acc: List[Message]): Unit = {
    logger.info("Sending messages to storage")
    store.storeMetrics(acc)
  }

  override protected def merge(acc: List[Message], elem: Message): List[Message] = acc :+ elem
}
