package ch.filefarmer.tests.importer
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.settings.ISettings
import scala.collection.mutable.ListBuffer
import ch.filefarmer.settings.DispatcherSettings
import com.google.inject.Injector
import ch.filefarmer.importer.IDispatcher
import ch.filefarmer.importer.DispatchDistributor
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import akka.testkit.TestActorRef
import ch.filefarmer.importer.Dispatcher
import java.util.HashSet
import com.google.inject.Provider

@RunWith(classOf[JUnitRunner])
class DispatchDistributorTests extends BaseTestClass {
	describe("The Dispatch distributor") {
		it("should create the dispatchers") {
			val settings = mock[ISettings]
			val provider = mock[Provider[IDispatcher]]
			
			//not really pretty, I now
			val dispatcherMock = TestActorRef(new Dispatcher(new HashSet(), settings))
			provider stubs 'get returning dispatcherMock.underlyingActor
			
			val dispatcherSettings = new ListBuffer[DispatcherSettings]
			dispatcherSettings += new DispatcherSettings("localhost", 10000, 3)
			
			inSequence {
			  settings expects 'dispatchers returning dispatcherSettings
			}
			
			val dispatchDistributor = TestActorRef(new DispatchDistributor(settings, provider))
			
			dispatchDistributor.underlyingActor.dispatchers should have length(3)
			dispatchDistributor.underlyingActor.dispatchers(0).homeAddress should be(None)
		}
	}
}