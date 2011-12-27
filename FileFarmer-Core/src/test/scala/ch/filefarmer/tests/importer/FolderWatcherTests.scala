package ch.filefarmer.tests.importer
import ch.filefarmer.tests.BaseTestClass
import akka.testkit.TestActorRef
import ch.filefarmer.importer.IDispatchDistributor
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.importer.FolderWatcher
import java.io.File
import java.nio.file.Files
import ch.filefarmer.poso.ArchiveFile
import akka.testkit.TestKit
import akka.util.duration._

@RunWith(classOf[JUnitRunner])
class FolderWatcherTests extends BaseTestClass with TestKit {
	describe("The folder watcher test") {
		it("should watch a folder for changes and pass it to the Distributor") {
			val mockDistributor = mock[IDispatchDistributor]
			mockDistributor stubs 'preStart
			
			val dispatchDistributor = TestActorRef(mockDistributor).start
			
			val testFolder = new File(currentPath + "/testWatchFolder")
			testFolder.mkdirs()
			val testFile = new File(currentPath + "/testFiles/test.png")
			val outputFile = new File(currentPath + "/testWatchFolder/test.png")
			outputFile.delete()
			
			inSequence {
				mockDistributor expects 'apply where { (x: Any) => x.isInstanceOf[ArchiveFile] }
			}
			
			val folderWatcher = new FolderWatcher(dispatchDistributor, "testWatchFolder")
			folderWatcher.start()
			Thread.sleep(1000)
			Files.copy(testFile.toPath, outputFile.toPath)
			Thread.sleep(1000)
			folderWatcher.stopWatcher()
			testFolder.delete()
		}
	}
}