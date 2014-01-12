package com.sneak.store.service.impl

import com.datastax.driver.core._
import scala.concurrent.{CanAwait, Future, ExecutionContext}
import scala.util.Try
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit
import com.typesafe.scalalogging.slf4j.Logging
import scala.util.Success
import scala.Some
import com.typesafe.config.Config

private[impl] trait CassandraResultSetOperations {

  private case class ExecutionContextExecutor(executonContext: ExecutionContext) extends java.util.concurrent.Executor {
    def execute(command: Runnable): Unit = { executonContext.execute(command) }
  }

  protected class RichResultSetFuture(resultSetFuture: ResultSetFuture) extends Future[ResultSet] {
    @throws(classOf[InterruptedException])
    @throws(classOf[scala.concurrent.TimeoutException])
    def ready(atMost: Duration)(implicit permit: CanAwait): this.type = {
      resultSetFuture.get(atMost.toMillis, TimeUnit.MILLISECONDS)
      this
    }

    @throws(classOf[Exception])
    def result(atMost: Duration)(implicit permit: CanAwait): ResultSet = {
      resultSetFuture.get(atMost.toMillis, TimeUnit.MILLISECONDS)
    }

    def onComplete[U](func: (Try[ResultSet]) => U)(implicit executionContext: ExecutionContext): Unit = {
      if (resultSetFuture.isDone) {
        func(Success(resultSetFuture.getUninterruptibly))
      } else {
        resultSetFuture.addListener(new Runnable {
          def run() {
            func(Try(resultSetFuture.get()))
          }
        }, ExecutionContextExecutor(executionContext))
      }
    }

    def isCompleted: Boolean = resultSetFuture.isDone

    def value: Option[Try[ResultSet]] = if (resultSetFuture.isDone) Some(Try(resultSetFuture.get())) else None
  }

  implicit def toFuture(resultSetFuture: ResultSetFuture): Future[ResultSet] = new RichResultSetFuture(resultSetFuture)
}

trait Binder[-A] {

  def bind(value: A, boundStatement: BoundStatement): Unit

}

trait BoundStatementOperations {

  implicit class RichBoundStatement[A : Binder](boundStatement: BoundStatement) {
    val binder: Binder[A] = implicitly[Binder[A]]

    def bindFrom(value: A): BoundStatement = {
      binder.bind(value, boundStatement)
      boundStatement
    }
  }

}

object cassandra {

  object resultset extends CassandraResultSetOperations

  object boundstatement extends BoundStatementOperations

}

private[impl] class CassandraClusterBuilder(config: Config) extends Logging {

  val CASSANDRA_SEEDS: String = "cassandra.hosts"

  val CASSANDRA_PORT: String = "cassandra.port"

  val port = config.getString(CASSANDRA_PORT).toInt

  val hosts = config.getString(CASSANDRA_SEEDS).split(",")

  lazy val build: Cluster = {
    Cluster.builder().
      addContactPoints(hosts: _*).
      withCompression(ProtocolOptions.Compression.SNAPPY).
      withPort(port).
      build()
  }

}