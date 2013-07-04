package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.{JsNumber, JsObject, JsValue}
import play.api.libs.iteratee.{Input, Iteratee, Enumerator}
import play.api.libs.concurrent.Promise
import models.{Statistics, Hosts}
import scala.concurrent.duration.DurationInt
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.Random
import java.util.Date
import play.api.libs.EventSource

object Application extends Controller {

  def index = Action {

    Ok( views.html.dashboard( "Dashboard", Hosts.hosts() ) )
  }

  def host( id: String ) = Action { implicit request =>
    Hosts.hosts.find( _.id == id ) match {
      case Some( host ) => Ok( views.html.host( host ) )
      case None => NoContent
    }
  }

  def stats( id: String ) = WebSocket.async[JsValue] { request =>
    Hosts.hosts.find( _.id == id ) match {
      case Some( host ) => Statistics.attach( host )
      case None => {
//        val enumerator = Enumerator
//          .generateM[JsValue]( Promise.timeout( None, 1.second ) )
//          .andThen( Enumerator.eof )
        val enumerator:Enumerator[JsValue] = Enumerator.enumInput(Input.EOF)
//          .generateM[JsValue]( Promise.timeout( None, 1.second ) )
//          .andThen( Enumerator.eof )
        Promise.pure( ( Iteratee.ignore[JsValue], enumerator ) )
      }
    }
  }

  def statsStream( id: String ) = Action { implicit request =>
    val promise = Hosts.hosts.find( _.id == id ) match {
      case Some( host ) => Statistics.attach( host ).map(pair => pair._2)
      case None => {
        //        val enumerator = Enumerator
        //          .generateM[JsValue]( Promise.timeout( None, 1.second ) )
        //          .andThen( Enumerator.eof )
        val enumerator:Enumerator[JsValue] = Enumerator.enumInput(Input.EOF)
        //          .generateM[JsValue]( Promise.timeout( None, 1.second ) )
        //          .andThen( Enumerator.eof )
        Promise.pure(  enumerator  )
      }
    }


    Async{
      promise.map(enumerator => Ok.stream(enumerator &> EventSource()).as("text/event-stream"))
    }

  }
  
}