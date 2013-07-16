package models

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Iteratee
import play.api.libs.json.JsValue

case class Refresh()
case class Connect( host: Host )
case class Connected( enumerator: Enumerator[ JsValue ] )

object Statistics {
  implicit val timeout = Timeout( 5 second )
  var actors: Map[ String, ActorRef ] = Map()

  def actor( id: String ) = actors.synchronized {
    actors.find( _._1 == id ).map( _._2 ) match {
      case Some( actor ) => actor
      case None => {
        val actor = Akka.system.actorOf( Props( new StatisticsActor(id) ), name = s"host-$id" )
        Akka.system.scheduler.schedule( 0.seconds, 3.second, actor, Refresh )
        actors += ( id -> actor )
        actor
      }
    }
  }

  def attach( host: Host ): Future[ ( Iteratee[ JsValue, _ ], Enumerator[ JsValue ] ) ] = {
    ( actor( host.id ) ? Connect( host ) ).map {
      case Connected( enumerator ) => ( Iteratee.ignore[JsValue], enumerator )
    }
  }
}

import java.util.Date

import scala.util.Random

import akka.actor.Actor
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsNumber
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsValue

class StatisticsActor( hostId: String ) extends Actor {
  val ( enumerator, channel ) = Concurrent.broadcast[JsValue]

  def receive = {
    case Connect( host ) => sender ! Connected( enumerator )
    case Refresh => broadcast( new Date().getTime(), hostId )
  }

  def broadcast( timestamp: Long, id: String ) {
    val msg = JsObject(
      Seq(
        "id" -> JsString( id ),
        "cpu" -> JsObject(
          Seq(
            ( "timestamp" -> JsNumber( timestamp ) ),
            ( "load" -> JsNumber( Random.nextInt( 100 ) ) )
          )
        )
      )
    )

    channel.push( msg )
  }
}