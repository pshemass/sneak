package com.sneak.store.core

import akka.actor.{Props, Actor}
import com.sneak.store.service.MetricsStore
import com.sneak.thrift.Message
import com.typesafe.scalalogging.slf4j.Logging
import WritingActor._
import com.sneak.store.service.impl.CassandraMetricStore


object WritingActor {

  sealed trait Operation {
    def id: Long
  }

  case class Save(id: Long, payload: Message) extends Operation

  case class BatchSave(id: Long, payload: Seq[Message]) extends Operation

  sealed trait OperationReply {
    def id: Long
  }

  case class Saved(id: Long) extends OperationReply

  def props(store: MetricsStore) = Props(classOf[WritingActor], store)

}

/**
 * Write messages to storage.
 */
class WritingActor(store: MetricsStore) extends Actor with Logging {

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

}
