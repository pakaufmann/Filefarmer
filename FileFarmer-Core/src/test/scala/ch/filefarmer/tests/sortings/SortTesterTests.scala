package ch.filefarmer.tests.sortings
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.workflows.sortings.SortTester
import ch.filefarmer.workflows.sortings.Sort
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SortTesterTests extends BaseTestClass {
	describe("the sort tester") {
	  it("should use the first correct sort definition") {
	    val sortTester = new SortTester
	    val def1 = Sort to 'archive1 where((file) => {
	    	false
	    })
	    val def2 = Sort to 'archive2 where((file) => {
	    	true
	    })
	    sortTester += def1
	    sortTester += def2 
	    
	    val sortDefinition = sortTester.testFile(new ArchiveFile(_originalFile = new File(".")))
	    
	    sortDefinition should be(Some(def2))
	  }
	  it("should return none if no sort definition was found") {
	    val sortTester = new SortTester
	    
	    val sortDefinition = sortTester.testFile(new ArchiveFile(_originalFile = new File(".")))
	    
	    sortDefinition should be(None)
	  }
	}
}