package com.sneak.store.core

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen, Prop}

class Specs2WithScalaCheck extends Specification with ScalaCheck {

  "Using Specs2 With ScalaCheck".title ^
    "Can be used with the check method" ! usePlainCheck

  def usePlainCheck = check((x: Int, y: Int) => {
    (x + y) must be_==(y + x)
  })
}