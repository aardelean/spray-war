package home.endpoints

import java.util.Date

import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import akka.util.Timeout
import home.config.InfinispanConfiguration
import home.config.PersistenceConfiguration._
import home.endpoint.ServiceActor
import home.model.Person
import akka.pattern._
import spray.http.HttpMethods._
import scala.concurrent.Await
import scala.concurrent.duration._
import spray.http.{HttpRequest, HttpResponse, Uri}
import spray.routing.Directives._
import spray.routing.HttpServiceActor

/**
 * Created by alex on 3/28/2015.
 * Simple actor
 */
class Endpoint() extends HttpServiceActor with ActorLogging{
  implicit val timeout : Timeout = 5 seconds
  val actor : ActorRef = context.actorOf(Props[ServiceActor])

  override def receive = runRoute {
    pathPrefix("spray") {
      path("persist") {
        get {
          parameter('p) { (p) =>
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
      } ~ path("actor") {
        get {
          complete {
            Await.result((actor ? "TEST"), timeout.duration).asInstanceOf[String]
          }
        }
      }
    }
  }
}
