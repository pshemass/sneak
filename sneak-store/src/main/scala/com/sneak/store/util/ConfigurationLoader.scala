package com.sneak.store.util

import com.typesafe.config.{Config, ConfigFactory}
import java.util.Properties

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

  implicit def toProperties(config: Config): Properties = {
    import scala.collection.JavaConverters._
    val props = new Properties()
    config.entrySet().asScala.foreach( entry => props.setProperty(entry.getKey(), entry.getValue.toString ))
    props
  }

}

