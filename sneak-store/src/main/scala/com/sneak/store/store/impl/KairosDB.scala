package com.sneak.store.store.impl

import spray.json._
import com.sneak.thrift.Message
import spray.http.ContentTypes

class MetricsWriter extends RootJsonWriter[Message] {
  override def write(obj: Message): JsValue = ???
}

object KairosDBProtocol extends DefaultJsonProtocol {

  implicit object ColorJsonFormat extends RootJsonFormat[Message] {
    def write(msg: Message) = {
      val tags = msg.options.mapValues[JsValue](v => JsString(v)) +
        "host" -> JsString(msg.host) +
        "application" -> JsString(msg.application)
      JsObject(
        "name" -> JsString(msg.name),
        "datapoints" -> JsNumber(msg.value),
        "tags" -> JsObject(tags)
      )
    }

    def read(value: JsValue) = {
      value.asJsObject.getFields("name", "datapoints", "tags") match {
        case Seq(JsString(name), JsNumber(value), JsString(tags)) =>
          Message(0l, name, value.toDouble, null, null, null)
        case _ => throw new DeserializationException("Message expected")
      }
    }
  }
}