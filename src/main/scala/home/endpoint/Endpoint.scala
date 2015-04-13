package home.endpoints

import java.util.Date

import akka.actor.{Actor, ActorLogging}
import home.config.InfinispanConfiguration
import home.config.PersistenceConfiguration._
import home.model.Person
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
    path("persist") {
      get {
        parameter('p){ (p)=>
          transactional() {
            new Person(new Date().toString)
          }
          complete("persisted")
        }
      }
    } ~ path("index") {
      get {
        parameter('key) {
          (key) => {
            val value = InfinispanConfiguration.cache.get(key)
            if (value != null) {
              complete(value)
            } else {
              complete("COULD NOT FIND!")
            }
          }
        }
      } ~
        post {
          formFields('key.as[String], 'value.as[String]) {
            (key: String, value: String) => {
              InfinispanConfiguration.cache.put(key, value)
              complete("SUCCESS " + key)
            }
          }
        }
    } ~ path("landingPage"){
      get{
        getFromResource("application.conf")
      }
    }
  }
}
