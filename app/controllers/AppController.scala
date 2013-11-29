package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.concurrent._
import akka.actor._

class TimerActor extends Actor {
  def receive = {
    case _ =>
  }
}
object TimerActor {
  case object Start
  case object Stop
}

object AppController extends Controller {
  import TimerActor._
  val timerActor = Akka.system.actorOf(Props[TimerActor])

  def index = Action {
    Ok(views.html.app.index())
  }

  def start = Action { implicit request => 
    timerActor ! Start
    Ok("")
    }

  def stop = Action { implicit request => 
    timerActor ! Stop
    Ok("")
    }

  def indexWS = TODO

  def jsRoutes = Action { implicit request =>
    Ok(Routes.javascriptRouter("jsRoutes")(routes.javascript.AppController.start, routes.javascript.AppController.stop, routes.javascript.AppController.indexWS))
  }
}