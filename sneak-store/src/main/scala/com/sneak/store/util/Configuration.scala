package com.sneak.store.util

/**
 * Configuration service.
 *
 * User: fox
 * Date: 8/24/13
 * Time: 11:52 PM
 */
trait Configuration {

  /**
   * Get a value of property if exists. If property is not found or null default argument is being evaluated
   * and it's value returned.
   * @param property Property value
   * @param default Value returned if property is not found or null
   * @return Returns property value or default
   */
  def getString(property: String, default: => String = ""): String

}
