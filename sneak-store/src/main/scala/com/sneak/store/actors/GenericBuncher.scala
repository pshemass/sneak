package com.sneak.store.actors



import akka.actor.ActorRefFactory
import scala.reflect.ClassTag
import scala.concurrent.duration.Duration
import akka.actor.{ FSM, Actor, ActorRef }
import scala.concurrent.duration.FiniteDuration

/**
 * Based on https://github.com/akka/akka/blob/master/akka-samples/akka-sample-fsm/src/main/scala/Buncher.scala
 * implementation. Buncher will collect set of messages and bunch them together by invoking a single action.

 * generic typed object buncher.
*
* To instantiate it, use the factory method like so:
*   Buncher(100, 500)(x : List[AnyRef] => x foreach println)
* which will yield a fully functional ActorRef.
* The type of messages allowed is strongly typed to match the
* supplied processing method; other messages are discarded (and
* possibly logged).
*/
object GenericBuncher {
  trait State
  case object Idle extends State
  case object Active extends State

  case object Flush // send out current queue immediately
  case object Stop // poison pill

  class MsgExtractor[A: ClassTag] {
    def unapply(m: AnyRef): Option[A] =
      if (implicitly[ClassTag[A]].runtimeClass isAssignableFrom m.getClass)
        Some(m.asInstanceOf[A])
      else
        None
  }
}

abstract class GenericBuncher[A: ClassTag, B](val singleTimeout: FiniteDuration, val multiTimeout: FiniteDuration)
  extends Actor with FSM[GenericBuncher.State, B] {
  import GenericBuncher._
  import FSM._

  protected def empty: B
  protected def merge(acc: B, elem: A): B
  protected def send(acc: B): Unit

  val timerName: String = "multi"

  protected def flush(acc: B) = {
    send(acc)
    cancelTimer(timerName)
    goto(Idle) using empty
  }

  val Msg = new MsgExtractor[A]

  startWith(Idle, empty)

  when(Idle) {
    case Event(Msg(m), acc) =>
      setTimer(timerName, StateTimeout, multiTimeout, false)
      goto(Active) using merge(acc, m)
    case Event(Flush, _) => stay
    case Event(Stop, _)  => stop
  }

  when(Active, stateTimeout = singleTimeout) {
    case Event(Msg(m), acc) =>
      stay using merge(acc, m)
    case Event(StateTimeout, acc) =>
      flush(acc)
    case Event(Flush, acc) =>
      flush(acc)
    case Event(Stop, acc) =>
      send(acc)
      cancelTimer(timerName)
      stop
  }

  initialize()
}

object Buncher {
  case class Target(target: ActorRef) // for setting the target for default send action

  val Stop = GenericBuncher.Stop // make special message objects visible for Buncher clients
  val Flush = GenericBuncher.Flush
}

class Buncher[A: ClassTag](singleTimeout: FiniteDuration, multiTimeout: FiniteDuration)
  extends GenericBuncher[A, List[A]](singleTimeout, multiTimeout) {

  import Buncher._

  private var target: Option[ActorRef] = None
  protected def send(acc: List[A]): Unit = if (target.isDefined) target.get ! acc.reverse

  protected def empty: List[A] = Nil

  protected def merge(l: List[A], elem: A) = elem :: l

  whenUnhandled {
    case Event(Target(t), _) =>
      target = Some(t)
      stay
  }

}