Client implementation
===
```scala
val settings = Settings("localhost:666", "sneak")
val publisher = KafkaEventPublisher(settings)
val system = MonitoringSystem(settings, publisher)
val captor = system.metricCaptor("test", "testApp")
captor.capture("test.metric", 666)
```
