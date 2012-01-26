package ch.filefarmer.tests.sortings
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.workflows.sortings.Sort
import ch.filefarmer.workflows.sortings.ISortTester
import ch.filefarmer.workflows.sortings.SortTester
import ch.filefarmer.poso.ArchiveFile
import java.io.File

@RunWith(classOf[JUnitRunner])
class SortDefinitionTests extends BaseTestClass {
  describe("the Sort") {
    val sortTester = new SortTester()
    Sort.sortTester = sortTester
    
    it("should return a SortDefinition") {
      val sortDefinition = Sort to 'test
      
      sortDefinition.archiveName.toString should be("'test") 
    }
    it("should add the itself to the sort tester") {
      val sortDefinition = Sort to 'test
      
      sortTester.sortDefinitions should contain(sortDefinition)
    }
    it("should return true if the when-section is true") {
      var calledFunc = false
      val sortDefinition = Sort to 'test where((file) => {
    	  calledFunc = true
    	  true
      })
      
      val retVal = sortDefinition.checkFile(new ArchiveFile())
      
      calledFunc should be(true)
      retVal should be(true)
    }
    it("should return false if the when-section is false") {
      var calledFunc = false
      val sortDefinition = Sort to 'test where((file) => {
    	  calledFunc = true
    	  false
      })
      
      val retVal = sortDefinition.checkFile(new ArchiveFile())
      
      calledFunc should be(true)
      retVal should be(false)
    }
  }
}