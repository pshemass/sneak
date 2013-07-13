import java.net.InetAddress
import org.joda.time._

/**
 * Representation of event information
 * User: fox
 * Date: 7/11/13
 * Time: 11:40 PM
 */
case class Metric(date: DateTime, name: String, value: Double, host: String, attributes: Map[String, String] = Map.empty) {

  def apply(name: String, value: Double, attributes: Map[String, String] = Map.empty) = {
    Metric(DateTime.now(), name, value, host, attributes)
  }

}

object Metric {

  val host = InetAddress.getLocalHost.getHostName


}