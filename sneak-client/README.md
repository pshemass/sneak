Client implementation
=====================
Every metric consists of the following information:
* name - name identifying the metric
* value - a numeric value. Can express response time in ms or heap usage in % 
* host - host the metric is collected from
* options - map describing arbitrary attributes identifying metric source
* application - name of the application that generated the metric

API usage
=========
```scala
val settings = Settings(kafkaZooKeeper="localhost:666", topic="sneak")
val publisher = KafkaEventPublisher(settings)
val system = MonitoringSystem(settings, publisher)
val captor = system.metricCaptor(host="myhost", applicationName="testApp")
captor.capture("test.metric", 666)
```
