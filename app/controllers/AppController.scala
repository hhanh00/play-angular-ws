package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent._
import akka.actor._
import rx.lang.scala.Observable
import play.api.libs.iteratee._
import play.api.libs.iteratee.Concurrent.Channel

class SourceActor extends FSM[SourceActor.State, SourceActor.Data] {
  import SourceActor._
  startWith(Disconnected, Unsubscribed)

  when(Disconnected) {
    case Event(Open(channel), Unsubscribed) =>
      goto(Connected) using Subscribed(channel)
  }

  when(Connected) {
    case Event(Close, _: Subscribed) =>
      goto(Disconnected) using Unsubscribed
  }

  initialize()
}

object SourceActor {
  trait State
  case object Disconnected extends State
  case object Connected extends State
  trait Data
  case object Unsubscribed extends Data
  case class Subscribed(channel: Channel[String]) extends Data

  case class Open(channel: Channel[String])
  case object Close
}

object AppController extends Controller {
  val system = ActorSystem()
  implicit val ec = system.dispatcher
  val sourceActor = system.actorOf(Props[SourceActor])
  
  def index = Action {
    Ok(views.html.app.index())
  }

  def start = Action { implicit request =>
    val source = Observable(0 until 10000)

    Ok("")
  }

  def stop = Action { implicit request =>
    Ok("")
  }

  def indexWS = WebSocket.using[String] { request =>
    val in = Iteratee.consume[String]().map { _ => sourceActor ! SourceActor.Close }
    val (out, channel) = Concurrent.broadcast[String]
    sourceActor ! SourceActor.Open(channel)
    (in, out)
  }

  def jsRoutes = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(routes.javascript.AppController.start, routes.javascript.AppController.stop, routes.javascript.AppController.indexWS))
  }
}