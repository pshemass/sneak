package com.sneak.store.util

import org.specs2.mutable._
import scala.util.Properties
import org.specs2.specification.AllExpectations
import com.typesafe.config.ConfigException


/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 9/12/13
 * Time: 11:06 PM
 */
class ConfigurationLoaderSpec extends Specification with AllExpectations {

  "Configuration loader" should {
    "correctly load a valid configuration" in {
      Properties.setProp("config.resource", "valid-config.conf")
      val config = ConfigurationLoader.load()
      val propValue = config.getInt("sneak.test.property")
      propValue mustEqual  "666"
    }

    "should fail validation of invalid config" in {
      Properties.setProp("config.resource", "invalid-config.conf")
      ConfigurationLoader.load() must throwA[ConfigException]
    }
  }


}
