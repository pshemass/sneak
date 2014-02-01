package com.sneak.store.storm

import storm.scala.dsl.StormBolt
import backtype.storm.tuple.Tuple

/**
 * Bolt that aggregates events
 */
class AggregatingBolt extends StormBolt(outputFields = List("metric")) {

  def execute(p1: Tuple): Unit = ???

}
