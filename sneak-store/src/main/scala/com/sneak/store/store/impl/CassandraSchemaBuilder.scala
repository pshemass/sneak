package com.sneak.store.store.impl

import com.typesafe.scalalogging.slf4j.Logging
import com.datastax.driver.core.Cluster

/**
 * Builds schema for Cassandra store.
 * User: fox
 * Date: 10/13/13
 * Time: 11:12 PM
 */
class CassandraSchemaBuilder extends Logging {

  def prepareSchema(cluster: Cluster, keyspace: String) = {
    logger.info(s"Creating new schema")
    val session = cluster.connect()
    val metadata = session.getCluster.getMetadata.getKeyspace(keyspace)
    if(metadata == null) {
      logger.info(s"Creating keyspace $keyspace")
      session.execute(
        s"""
          |CREATE KEYSPACE $keyspace WITH replication
          |= {'class':'SimpleStrategy', 'replication_factor':3};
        """.stripMargin)
      logger.info(s"Creating table messages")
      session.execute(
        s"""
          |CREATE TABLE $keyspace.messages (
          |id uuid PRIMARY KEY,
          |event_time timestamp,
          |name text,
          |value double,
          |host text,
          |application text,
          |options map<text,text>
          |);
        """.stripMargin)
      val indexQuery =
        s"""
          |create index on ${keyspace}.messages(event_time)
        """.stripMargin
      logger.info("Adding index to messages table.")
      session execute( indexQuery)
      logger.info("Schema creation complete")
    } else {
      logger.info("Schema already exists.")
    }
  }

}
