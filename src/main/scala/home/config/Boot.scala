package home.config

import akka.actor.{Props, ActorSystem}
import home.endpoints.Endpoint
import org.infinispan.manager.DefaultCacheManager
import spray.routing.SimpleRoutingApp
import home.endpoints.Index
import net.fwbrasil.activate.ActivateContext
import net.fwbrasil.activate.storage.relational.PooledJdbcRelationalStorage
import net.fwbrasil.activate.storage.relational.idiom.mySqlDialect
import spray.servlet.WebBoot

// This class is instantiated by the servlet initializer.
// It can either define a constructor with a single
// `javax.servlet.ServletContext` parameter or a
// default constructor.
// It must implement the spray.servlet.WebBoot trait.
class Boot extends WebBoot {

  InfinispanConfiguration.cache
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("webapp")

  // the service actor replies to incoming HttpRequests
  val serviceActor = system.actorOf(Props[Endpoint], "endpoint")
  // the service actor replies to incoming HttpRequests


  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }
}
