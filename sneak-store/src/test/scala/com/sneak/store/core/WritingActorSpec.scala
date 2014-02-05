package com.sneak.store.core


import akka.actor.ActorSystem
import org.scalatest.{WordSpecLike, BeforeAndAfterAll}
import akka.testkit.{ ImplicitSender, TestKit }
import com.sneak.store.store.MetricsStore
import com.sneak.thrift.Message
import com.sneak.store.actors.WritingActor
import org.specs2.mock.Mockito
import com.sneak.store._
import scala.concurrent.duration._

class WritingActorSpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender
  with WordSpecLike
  with Mockito
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("WritingActorSpec"))


  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "WritingActor" must {

    "confirm message save" in {
      val store: MetricsStore = mock[MetricsStore]
      val writerActor = system.actorOf(WritingActor.props(store, 100 millis, 100 millis))
      val msgs: Seq[Message] = msgSequenceGenerator(10)
      msgs.foreach(writerActor ! _)

      there was one(store).storeMetrics(msgs)
    }

  }

}
