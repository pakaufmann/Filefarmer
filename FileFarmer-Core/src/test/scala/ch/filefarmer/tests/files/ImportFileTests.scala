package ch.filefarmer.tests.files
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ImportFileTests extends BaseTestClass {
	describe("the base test class") {
	  it("should contain various parameters") {
	    val importFile = new ArchiveFile(originalFile = new File(currentPath + "/testFiles/test.png"))
	    
	    importFile.getExtension should be("png")
	    importFile.fileName should be("test.png")
	    importFile.tiffFile should be(null)
	    importFile.fullText should be("")
	  }
	}
}