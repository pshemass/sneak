package com.sneak.store.service.impl

import com.sneak.thrift.Message
import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.util.Configuration
import com.datastax.driver.core._
import java.util.{Date, UUID}
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.sneak.store.service.MetricsStore

/**
 * Store that persists messages to Cassandra database.
 *
 * User: fox
 * Date: 8/8/13
 * Time: 10:52 PM
 */
class CassandraMetricStore(config: Configuration,
                           cluster: Cluster,
                           keyBuilder: Message => String = message => message.name)
extends MetricsStore with Logging {

  val KEYSPACE_PROPERTY = "cassandra.keyspace"

  val keyspace = config getString KEYSPACE_PROPERTY

  val session = cluster.connect

  class MetricBinder extends Binder[Message] {
    def bind(value: Message, boundStatement: BoundStatement): Unit = ???
  }

  implicit val binder = new MetricBinder

  def close {
    logger info s"Shutting down connection to Cassandra cluster ${cluster.getMetadata.getClusterName}"
    //cluster shutdown will also close all sessions
    cluster.shutdown()
  }

  import cassandra.resultset._

  def storeMetric(metric: Message): Future[ResultSet] = {

    import cassandra.boundstatement._

    logger.info(s"Storing message ${metric.name}")
    val stmt: PreparedStatement = session.prepare(
      s"""
        |INSERT INTO $keyspace.messages(id, event_time, name, value, host, application, options)
        |VALUES(?,?,?,?,?,?,?);
      """.stripMargin)
    val boundStatement = new BoundStatement(stmt)
    session executeAsync (boundStatement bindFrom metric)
  }


  def readMetric(key: String): Future[Message] = {
    logger.info(s"Reading metric for $key")
    val query =
      s"""
        |select * from $keyspace.messages
        |where id = ?
      """.stripMargin
    val stmt = session.prepare(query)
    val boundStatement = new BoundStatement(stmt)
    session.executeAsync(boundStatement.bind(UUID.fromString(key))) map (_.all.map(buildMessage).toList.head)
  }

  def buildMessage(row: Row): Message = {
    Message(
      row.getDate(1).getTime,
      row.getString(2),
      row.getDouble(3),
      row.getString(4),
      row.getString(5),
      row.getMap(6, classOf[String], classOf[String])
    )
  }

}

object CassandraMetricStore {

  def apply(config: Configuration): CassandraMetricStore = {
    val builder = new CassandraClusterBuilder(config)
    val cluster = builder.build
    new CassandraMetricStore(config, cluster) //TODO: consider a solution for key builder
  }
}
