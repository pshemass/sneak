package com.sneak.store.actors

import akka.actor.{Props, Actor}
import com.sneak.store.store.MetricsStore
import com.sneak.thrift.Message
import com.typesafe.scalalogging.slf4j.Logging
import WritingActor._
import scala.concurrent.duration.FiniteDuration


object WritingActor {

  sealed trait Operation {
    def id: Long
  }

  case class Save(id: Long, payload: Message) extends Operation
  case class BatchSave(id: Long, payload: Seq[Message]) extends Operation

  trait State
  case object Idle extends State
  case object Active extends State

  case object Flush // send out current queue immediately
  case object Stop // poison pill


  sealed trait OperationReply {
    def id: Long
  }

  /**
   * Reply message
   * @param id Id of message written.
   */
  case class Saved(id: Long) extends OperationReply

  def props(store: MetricsStore) = Props(classOf[WritingActor], store)

}

/**
 * Write messages to storage. The actor extends GenericBuncher trait to batch event updates.
 */
class WritingActor(store: MetricsStore)(singleTimeout: FiniteDuration, multiTimeout: FiniteDuration)
  extends GenericBuncher[Message, List[Message]](singleTimeout, multiTimeout)
  with Logging {

  def saveMessage(message: Message) {
    logger.info("Storing message {}", message.name)
    store.storeMetric(message)
  }

  override def receive: Receive = {
    case batch @ BatchSave(id, payload) => {
      batch.payload.foreach(saveMessage(_))
      sender ! Saved(id)
    }
    case save @ Save(id, payload) => {
      saveMessage(payload)
      sender ! Saved(id)
    }
  }

  override protected def empty: List[Message] = Nil

  override protected def send(acc: List[Message]): Unit = {
    logger.info("Sending messages to storage")
    store.storeMetrics(acc)
  }

  override protected def merge(acc: List[Message], elem: Message): List[Message] = acc :+ elem
}
