package com.sneak

import org.scalacheck.{Arbitrary, Gen}
import com.sneak.thrift.Message
import org.scalacheck.Arbitrary._

/**
 * Various generators
 */
package object store {

  val genIntList      = Gen.containerOf[List,Int](Gen.oneOf(1, 3, 5))


  val msgGenerator: Gen[Message] = for {
    time <- arbitrary[Long].suchThat( _ > 0)
    name <- Gen.oneOf("metric1", "metric2", "metric3")
    value <- Gen.choose(0, 100)
    host <- Gen.oneOf("host1", "host2")
    application <- Gen.oneOf("app1", "app2")
  } yield Message(time, name, value, host, application)

  def msgSequenceGenerator(n: Int) = Gen.containerOfN[Seq, Message](n, msgGenerator)

  implicit def randomVal[V](gen: Gen[V]) = gen.sample.get

}
