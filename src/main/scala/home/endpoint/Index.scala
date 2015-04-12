package home.endpoints

import akka.actor.{Actor, ActorLogging}
import home.config.persistenceContext
import home.model.{ Person}
import spray.http.HttpMethods._
import spray.http.{HttpRequest, HttpResponse, Uri}
/**
 * Created by alex on 3/28/2015.
 * Simple actor
 */
class Index() extends Actor with ActorLogging{

  override def receive = {
    case HttpRequest(GET, Uri.Path("/index"), _, _, _) =>
      persistenceContext.transactional(){
        new Person("WITHOUT RESTART")
      }
      sender ! HttpResponse(entity = "Test")
  }

}
