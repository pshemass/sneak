package com.sneak.store.actors

import org.specs2.mutable.Specification
import org.scalacheck.Arbitrary.arbitrary
import com.sneak.store._
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import org.specs2.mock.Mockito
import com.sneak.store.actors.Buncher.Target
import scala.concurrent.duration._
import akka.actor.FSM.Event
import org.scalacheck.Gen

class GenericBuncherTest(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("ActorSpec"))

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "Generic buncher" should {

    "send all messages after interval" in {
      //given
      val buncherActor = system.actorOf(Props(new Buncher[String](100 millis, 100 millis)))
      val probe = TestProbe()
      buncherActor ! Target(probe.ref)
      val msg: String = arbitrary[String]

      //when
      buncherActor ! msg

      //then
      probe.expectMsg(500 millis, List(msg))
    }


    "send bunch of messages after threshold met" in {
      //given
      val buncherActor = system.actorOf(Props(new Buncher[String](100 millis, 100 millis)))
      val probe = TestProbe()
      buncherActor ! Target(probe.ref)
      val msgsGen: Gen[List[String]] = Gen.containerOfN[List,String](100, arbitrary[String])
      val msgs = msgsGen.sample.get

      //when
      msgs.foreach(msg => buncherActor ! msg)

      //then
      probe.expectMsg(500 millis, msgs)
    }

  }

}
