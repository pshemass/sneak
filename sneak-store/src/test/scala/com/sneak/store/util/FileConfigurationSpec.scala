package com.sneak.store.util

import org.specs2.mutable._
import scala.util.Properties
import org.specs2.specification.AllExpectations


/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 9/12/13
 * Time: 11:06 PM
 */
class FileConfigurationSpec extends Specification with AllExpectations {

  "File configuration" should {
    "correctly read file" in {
      val props = FileConfiguration("test.properties")
      val propValue = props.getString("test.property")
      propValue mustEqual  "666"
    }

    "load properties from file given by system property" in {
      Properties.setProp(FileConfiguration.CONFIG_FILE_PROPERTY, "test.properties")
      val props = FileConfiguration()
      val propValue = props.getString("test.property")
      propValue mustEqual  "666"
    }
  }


}
