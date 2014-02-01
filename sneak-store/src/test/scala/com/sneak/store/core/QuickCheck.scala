package com.sneak.store.core

import org.specs2.mutable.Specification
import org.scalacheck.Gen

/**
 * Created by fox on 01/02/14.
 */
class QuickCheck extends Specification {

  def sample(n: Int):Gen[Seq[Int]] = Gen.containerOfN[List,Int](n, Gen.oneOf(1, 3, 5))

  val sampleR:Gen[Seq[Int]] = Gen.nonEmptyContainerOf[List,Int](Gen.oneOf(1, 3, 5))

  "generates list" should {

    "of ints" in {
      val sizeGen = Gen.choose(1,20)
      val someSize = sizeGen.sample.get
      sample(someSize).sample.get.size must beEqualTo(someSize)
    }

    "has random size" in {
      val seq = sampleR.sample.get
      seq.size must beGreaterThan(0)
    }
  }
}
