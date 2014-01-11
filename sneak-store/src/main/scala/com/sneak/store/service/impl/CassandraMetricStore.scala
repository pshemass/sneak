package com.sneak.store.service.impl

import com.sneak.thrift.Message
import com.typesafe.scalalogging.slf4j.Logging
import com.sneak.store.util.Configuration
import com.sneak.store.service.MetricsStore
import com.datastax.driver.core._
import java.util.{Date, UUID}
import com.datastax.driver.core.querybuilder.{Clause, QueryBuilder}
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Store that persists messages to Cassandra database.
 *
 * User: fox
 * Date: 8/8/13
 * Time: 10:52 PM
 */
class CassandraMetricStore(config: Configuration,
                           clusterFactory: CassandraClusterFactory,
                           keyBuilder: Message => String = message => message.name)
  extends MetricsStore with Logging {

  val KEYSPACE_PROPERTY = "cassandra.keyspace"

  val keyspace = config getString(KEYSPACE_PROPERTY)

  val cluster: Cluster = clusterFactory.connect

  def close {
    logger info s"Shutting down connection to Cassandra cluster ${cluster.getMetadata.getClusterName}"
    cluster.shutdown()
  }


  def storeMetric(metric: Message) = {
    import scala.collection.JavaConverters._

    logger.info(s"Storing message ${metric.name}")
    val session: Session = cluster.connect()
    val stmt: PreparedStatement = session.prepare(
      s"""
        |INSERT INTO ${keyspace}.messages(id, event_time, name, value, host, application, options)
        |VALUES(?,?,?,?,?,?,?);
      """.stripMargin)
    val boundStatement = new BoundStatement(stmt)
    session executeAsync(boundStatement bind(
      UUID randomUUID(),
      new Date(metric timestamp),
      metric name,
      metric value : java.lang.Double,
      metric host,
      metric application,
      metric.options.asJava
    ))
  }

  import cassandra.resultset._


  def readMetric(key: String): Future[Message] = {
    logger.info(s"Reading metric for $key")
    val query =
      s"""
        |select * from ${keyspace}.messages
        |where id = ?
      """.stripMargin
    val session = cluster.connect
    val stmt = session.prepare(query)
    val boundStatement = new BoundStatement(stmt)
    session.executeAsync( boundStatement.bind(UUID.fromString(key))) map(_.all.map(buildMessage).toList.head)
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

  /**
   * Key space for storing metrics
   */
  val KeySpaceName = "metrics"

  /**
   * Column family for storing metrics
   */
  val ColumnFamily = "metrics"
}
