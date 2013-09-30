package com.sneak.store.util

import com.typesafe.scalalogging.slf4j.Logging
import java.io.{FileInputStream, File}
import java.util.Properties
import scala.sys.SystemProperties

/**
 * File based configuration.
 *
 * User: fox
 * Date: 8/25/13
 * Time: 12:34 AM
 */
class FileConfiguration(file: File) extends Configuration with Logging {

  val properties = loadProperties

  /**
   * Loads properties from file
   * @return Returns properties object
   */
  def loadProperties = {
    val props = new Properties()
    props.load(new FileInputStream(file))
    props
  }

  override def getString(property: String, defaultValue: => String): String = {
    properties.getProperty(property, "") match {
      case "" => defaultValue
      case someValue: String => someValue
    }
  }

}

object FileConfiguration {

  val CONFIG_FILE_PROPERTY = "storeConfigFile"

  /**
   * Creates new configuration from properties file location.
   * @param localPath Location of properties file
   * @return
   */
  def apply(localPath: String) = {
    val path = getClass.getClassLoader.getResource(localPath).getPath
    new FileConfiguration(new File(path))
  }

  /**
   * Creates new configuration from given properties file.
   * @param file Properties file location
   * @return Returns Configuration object
   */
  def apply(file: File) = new FileConfiguration(file)

  /**
   * Loads configuration from a predefined set of locations:
   * <ul>
   * <li>Location coming from system property <pre>-Dconfig.file</pre>
   * <li>Location in the config folder
   * </ul>
   */
  def apply(): FileConfiguration = {
    val filePath = scala.util.Properties.propOrElse(CONFIG_FILE_PROPERTY, configFile)
    apply(filePath)
  }

  /**
   *
   * @return Config file location in the current config folder of the application
   */
  def configFile: String = {
    val dir = scala.util.Properties.userDir
    val file = new File(dir, "sneak.properties")
    file.getPath
  }
}
