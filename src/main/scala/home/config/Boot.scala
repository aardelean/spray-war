package home.config

import akka.actor.{Props, ActorSystem}
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

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("webapp")

  // the service actor replies to incoming HttpRequests
  val serviceActor = system.actorOf(Props[Index], "index-test")

  system.registerOnTermination {
    // put additional cleanup code here
    system.log.info("Application shut down")
  }
}

object persistenceContext extends ActivateContext {
  val storage = new PooledJdbcRelationalStorage {
    val jdbcDriver = "com.mysql.jdbc.Driver"
    val user = new Some("root")
    val password = new Some("root")
    val url = "jdbc:mysql://localhost:3306/users"
    val dialect = mySqlDialect
  }
  override protected def entitiesPackages = List("home.model")
}
