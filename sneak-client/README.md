Client implementation
===
```scala
implicit val settings = Settings("broker.host:123", "sneak-topic")
val system = MonitoringSystem()
val captor = system.metricCaptor("my.host", "my.application")
captor.capture("responseTime", 666)
```
