import java.net.InetAddress
import org.joda.time.DateTime

/**
 * Representation of event information
 * User: fox
 * Date: 7/11/13
 * Time: 11:40 PM
 */
case class Metric(date: DateTime, name: String, value: Double, host: String, attributes: Map = Map.empty)

object Metric {

  val host = InetAddress.getLocalHost.getHostName

  def apply(name: String, value: Double, attributes: Map = Map.empty) = {
    Metric(DateTime.now(), name, value, host, attributes)
  }
}