package home.endpoints

import akka.actor.{ActorRef, Props, Actor, ActorLogging}
import akka.util.Timeout
import home.config.InfinispanConfig
import home.endpoint.ServiceActor
import spray.http.HttpMethods._
import spray.http.{HttpRequest, HttpResponse, Uri}
import spray.routing.Directives._
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.{ ask, pipe }
import spray.routing.HttpServiceActor

/**
 * Created by alex on 3/28/2015.
 * Simple actor
 */
class Endpoint() extends HttpServiceActor with ActorLogging{
  implicit val timeout = Timeout(5 seconds)
  val serviceActor: ActorRef = context.actorOf(Props[ServiceActor], "ServiceActor")

  override def receive = runRoute{
    path("persist") {
      get {
        parameter('key) {
          (key) => {
            val value = InfinispanConfig.cache.get(key);
            if (value!=null)  complete(value) else complete(s"NOT FOUND VALUE FOR KEY $key")
          }
        }
      } ~
      post {
        formFields('key.as[String] , 'value.as[String]) {
          (key :String , value :String) => {
           InfinispanConfig.cache.put(key, value)
            complete("SUCCESS "+key)
          }
        }
      }
    } ~ path("check"){
      get {
        complete("VERIFIED!")
      }
    } ~ path("actor"){
      get {
        complete{
            Await.result(( serviceActor ? "TEST"), timeout.duration).asInstanceOf[String]
          }
      }
    }
  }
}
