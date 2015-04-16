package home.endpoint

import akka.actor.Actor
import akka.actor.Actor.Receive
import spray.routing.Directives._

/**
 * Created by aardelean on 15.04.2015.
 */
class ServiceActor extends Actor{
  val name ="ServiceActor"
  override def receive: Receive = {
    case "TEST" => sender() ! "SERVICE DELEGATED!"
    case message@_ => sender ! s"UNKNOWN MESSAGE! $message";
  }
}
