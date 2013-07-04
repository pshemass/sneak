package models

/**
 * Created with IntelliJ IDEA.
 * User: rzbiq
 * Date: 7/1/13
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
case class Host( id: String, name: String )

object Hosts {
  def hosts(): List[ Host ] = {
    return List( new Host( "h1", "Host 1" ), new Host( "h2", "Host 2" ) )
  }
}
