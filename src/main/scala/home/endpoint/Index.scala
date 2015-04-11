package home.endpoints

import akka.actor.{Actor, ActorLogging}
import spray.http.HttpMethods._
import spray.http.{HttpRequest, HttpResponse, Uri}

/**
 * Created by alex on 3/28/2015.
 * Simple actor
 */
class Index() extends Actor with ActorLogging{

  override def receive = {
    case HttpRequest(GET, Uri.Path("/index"), _, _, _) =>
      sender ! HttpResponse(entity = "Test")
  }
}
