package home.endpoints

import akka.actor.{Actor, ActorLogging}
import home.config.InfinispanConfig
import spray.http.HttpMethods._
import spray.http.{HttpRequest, HttpResponse, Uri}
import spray.routing.Directives._
import spray.routing.HttpServiceActor

/**
 * Created by alex on 3/28/2015.
 * Simple actor
 */
class Endpoint() extends HttpServiceActor with ActorLogging{

  override def receive = runRoute{
    path("index") {
      get {
        parameter('key) {
          (key) => {
            complete(InfinispanConfig.cache.get(key))
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

    }
  }
}
