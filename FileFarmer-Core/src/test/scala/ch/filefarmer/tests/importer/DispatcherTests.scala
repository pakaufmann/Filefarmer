package ch.filefarmer.tests.importer
import ch.filefarmer.tests.BaseTestClass
import akka.testkit.TestActorRef
import ch.filefarmer.importer.actions.IAction
import ch.filefarmer.importer.Dispatcher
import java.util.HashSet
import java.util.UUID
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.settings.ISettings
import java.io.File
import ch.filefarmer.poso.ArchiveFile
import java.util.LinkedHashSet

@RunWith(classOf[JUnitRunner])
class DispatcherTests extends BaseTestClass {
	describe("The dispatcher should") {
		it("create a uuid") {
			val actionSet = new HashSet[IAction]()
			val settings = mock[ISettings]
			
			val dispatcher = TestActorRef(new Dispatcher(actionSet, settings))
			  
			try {
				evaluating {UUID.fromString(dispatcher.id)}
			} catch {
				case ex: IllegalArgumentException => fail("This sould be a correct UUID")
			}
		}
		
		it("should rename the file and go through all actions if they are ok") {
			val actionSet = new LinkedHashSet[IAction]
			val action1 = mock[IAction]
			action1 stubs 'hashCode returning 1
			val action2 = mock[IAction]
			action2 stubs 'hashCode returning 2
			actionSet.add(action1)
			actionSet.add(action2)
			
			val settings = mock[ISettings]
			val txtFile = new File(currentPath + "/testFiles/test.txt")
			val testFile = new File(currentPath + "/testFiles/dispatcherTest.txt") 
			testFile.renameTo(testFile)
			
			val dispatcher = TestActorRef(new Dispatcher(actionSet, settings)).start()
			
			inSequence {
			  settings expects 'tempFolder returning "testFiles"
			  action1 expects 'execute returning true
			  action2 expects 'execute returning true
			}
			
			dispatcher ! new ArchiveFile(originalFile = testFile)
		}
		
		it("should stop when an action returns false") {
			val actionSet = new LinkedHashSet[IAction]
			val action1 = mock[IAction]
			action1 stubs 'hashCode returning 1
			val action2 = mock[IAction]
			action2 stubs 'hashCode returning 2
			actionSet.add(action1)
			actionSet.add(action2)
			
			val settings = mock[ISettings]
			val txtFile = new File(currentPath + "/testFiles/test.txt")
			val testFile = new File(currentPath + "/testFiles/dispatcherTest.txt") 
			testFile.renameTo(testFile)
			
			val dispatcher = TestActorRef(new Dispatcher(actionSet, settings)).start()
			
			inSequence {
			  settings expects 'tempFolder returning "testFiles"
			  action1 expects 'execute returning false
			  action2 expects 'execute returning true never
			}
			
			dispatcher ! new ArchiveFile(originalFile = testFile)
		}
	}
}