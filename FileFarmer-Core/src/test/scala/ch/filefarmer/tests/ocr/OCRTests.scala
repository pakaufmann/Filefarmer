package ch.filefarmer.tests.ocr
import ch.filefarmer.tests.BaseTestClass
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.ocr._
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import ch.filefarmer.settings.ISettings

@RunWith(classOf[JUnitRunner])
class OCRTests extends BaseTestClass {
	val settings = mock[ISettings]
  
	describe("the ocr actions:") {
		it("the text ocr should just convert txt files") {
			val textOCR = new TextOCR()
			
			textOCR.formats should contain ("txt")
			textOCR.formats should have length(1)
		}
		it("the text ocr should just read out simple text files") {
			val textOCR = new TextOCR()
			
			val text = textOCR.doOCR(new ArchiveFile(originalFile = new File(currentPath + "/testFiles/test.txt")))
			
			text should be ("Test\nTest\nTest\nTest\nTest\n")
		}
		it("the standard ocr should be the standard ocr engine") {
			val standardOCR = new StandardOCR(settings)
			
			standardOCR.formats should contain ("standard")
			standardOCR.formats should have length (1)
		}
		it("the standard ocr should convert with the help of the tiff file") {
			val tiffFile = new File(currentPath + "/testFiles/test.tif")
			inSequence {
				settings expects 'tempFolder returning "testFiles"
			}
			
			val standardOCR = new StandardOCR(settings)
			val importFile = new ArchiveFile(originalFile = new File("."))
			importFile.tiffFile = tiffFile
			
			val text = standardOCR.doOCR(importFile)
			text should include("Das ist ein Test Das ist ein Test\n")
		}
	}
}