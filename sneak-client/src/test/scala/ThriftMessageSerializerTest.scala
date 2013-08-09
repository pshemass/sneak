import com.sneak.client.ThriftMessageSerializer
import com.sneak.thrift.Message
import java.util.Date
import org.specs2.mutable.Specification

/**
 * Created with IntelliJ IDEA.
 * User: fox
 * Date: 7/30/13
 * Time: 11:18 PM
 */
class ThriftMessageSerializerTest extends Specification  {

  "ThriftMessageSerializer" should {
    "Seriaize and Deserialize" in {
      val serializer = new ThriftMessageSerializer
      val msg = Message(System.currentTimeMillis(), "test", 1.0, "localhost", "unit-test")
      val bytes = serializer.toBytes(msg)
      val clone = serializer.fromBytes(bytes)
      clone mustEqual msg
    }
  }

}
