package com.sneak.store.util

import com.typesafe.config.{Config, ConfigFactory}

/**
 * Configuration service.
 *
 * User: fox
 * Date: 8/24/13
 * Time: 11:52 PM
 */
object ConfigurationLoader {

  def load(): Config = {
    val config = ConfigFactory.load()
    config.checkValid(ConfigFactory.defaultReference())
    config
  }

}