package ch.filefarmer.importer
import com.google.inject.Inject
import com.google.inject.Guice
import akka.actor._
import actions.IAction
import akka.routing.LoadBalancer
import ch.filefarmer.CoreModule
import akka.routing.SmallestMailboxFirstIterator
import scala.collection.mutable.ListBuffer
import ch.filefarmer.settings.ISettings
import com.google.inject.Provider

/**
 * the default implementation for the dispatch distributor
 * creates the dispatchers and provides dispatches the incoming files to them
 * 
 * @constructor creates a new dispatch distributor
 * @param settings the system settings
 * @param dispatcherProvider a provider to create the dispatchers
 */
class DispatchDistributor@Inject()(private val settings: ISettings, private val dispatcherProvider:Provider[IDispatcher]) extends IDispatchDistributor {
  private var numberOfFiles = 0
  
  val dispatchers = new ListBuffer[ActorRef]
  
  //create all dispatchers for the given hosts
  for(d <- settings.dispatchers) {
	  Actor.remote.start(d.host, d.port)
    
	  for(i <- 1 to d.numberOfDispatchers) {
		  val dispatcher = Actor.remote.actorOf(dispatcherProvider.get(), d.host, d.port).start()
		  dispatchers += dispatcher
	  }
  }
  
  val seq = new SmallestMailboxFirstIterator(dispatchers.toList)
}