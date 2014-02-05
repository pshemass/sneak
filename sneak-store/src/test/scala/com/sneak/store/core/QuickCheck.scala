package com.sneak.store.core

import org.specs2.mutable.Specification
import org.scalacheck.{Prop, Properties, Gen}

object QuickCheck extends Properties("Quickcheck playground") {

  def sample(n: Int):Gen[Seq[Int]] = Gen.containerOfN[List,Int](n, Gen.oneOf(1, 3, 5))

  val sampleR:Gen[Seq[Int]] = Gen.nonEmptyContainerOf[List,Int](Gen.oneOf(1, 3, 5))

  property("list is not empty") = Prop.forAll { ints: Seq[Int] =>
    ints.size >= 0
  }



}

