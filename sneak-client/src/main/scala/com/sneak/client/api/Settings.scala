package com.sneak.client.api

import java.net.InetSocketAddress

/**
 * Client settings
 */
case class Settings(kafkaZooKeeper: String,
                    topic: String)

object Converters {

  /**
   * Converts coma separated string to sequence of addresses.
   * @param addresses Set of addresses eg. localhost:1234,some.host.com:666
   * @return Returns sequence of InetSocketAddress
   */
  implicit def stringToInetSocketAddress(addresses: String): Seq[InetSocketAddress] = {
    addresses.split(",").map { address =>
      address.split(":").toList match {
        case host :: port :: Nil => new InetSocketAddress(host, port.toInt)
        case _ => throw new IllegalArgumentException(s"Zookeeper address ${address} is invalid")
      }
    }
  }

}