package com.sneak.store.core


import akka.actor.ActorSystem
import org.scalatest.{WordSpecLike, BeforeAndAfterAll}
import akka.testkit.{ ImplicitSender, TestKit }
import com.sneak.store.service.MetricsStore
import com.sneak.thrift.Message
import WritingActor._
import org.specs2.mock.Mockito

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
      val writerActor = system.actorOf(WritingActor.props(store))
      val msg = Message(System.currentTimeMillis(), "test", 1.0, "localhost", "app1")
      writerActor ! Save(1l, msg)

      expectMsg(Saved(1))
    }

    "confirm messages save" in {
      val store: MetricsStore = mock[MetricsStore]
      val writerActor = system.actorOf(WritingActor.props(store))
      val msg1 = Message(System.currentTimeMillis(), "test1", 1.0, "localhost", "app1")
      val msg2 = Message(System.currentTimeMillis(), "test2", 1.0, "localhost", "app1")
      writerActor ! BatchSave(1l, Seq(msg1, msg2))

      expectMsg(Saved(1))
    }

    "execute store operation" in {
      import akka.testkit.TestActorRef

      val store: MetricsStore = mock[MetricsStore]

      val actorRef = TestActorRef(WritingActor.props(store))
      val msg = Message(System.currentTimeMillis(), "test", 1.0, "localhost", "app1")
      actorRef receive(Save(1l, msg))

      there was no(store).storeMetric(any[Message])
    }

  }

}
