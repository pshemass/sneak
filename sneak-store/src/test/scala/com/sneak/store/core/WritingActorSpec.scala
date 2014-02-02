package com.sneak.store.core


import akka.actor.ActorSystem
import org.scalatest.{WordSpecLike, BeforeAndAfterAll}
import akka.testkit.{ ImplicitSender, TestKit }
import com.sneak.store.store.MetricsStore
import com.sneak.thrift.Message
import com.sneak.store.actors.WritingActor
import WritingActor._
import org.specs2._
import org.specs2.mock.Mockito
import com.typesafe.config.ConfigFactory
import org.scalacheck._
import com.sneak.store._
import Arbitrary.arbitrary

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
      val msg = msgGenerator.sample.get
      writerActor ! Save(1l, msg)

      expectMsg(Saved(1))
    }

    "confirm messages save" in {
      val store: MetricsStore = mock[MetricsStore]
      val writerActor = system.actorOf(WritingActor.props(store))
      val size: Int = arbitrary[Int]
      val msgs: Seq[Message] = msgSequenceGenerator(size)
      writerActor ! BatchSave(1l, msgs)

      expectMsg(Saved(1))
    }

  }

}

class WritingActorUnitTest extends mutable.Specification with Mockito {

  "execute store operation" in {
    import akka.testkit.TestActorRef

    implicit val actorSystem = ActorSystem("testActorSystem", ConfigFactory.load())

    val store: MetricsStore = mock[MetricsStore]

    val actorRef = TestActorRef(WritingActor.props(store))
    val msg: Message = msgGenerator
    actorRef receive(Save(1l, msg))

    there was one(store).storeMetric(any[Message])
  }
}