package ch.filefarmer.tests.converters
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import ch.filefarmer.settings.ISettings
import ch.filefarmer.converters.StandardFilesToTiffConverter
import ch.filefarmer.tests.BaseTestClass

@RunWith(classOf[JUnitRunner])
class StandardFilesToTiffFileConverterTests extends BaseTestClass {
	val settings = mock[ISettings]
	
	val standardFilesTiffConverter = new StandardFilesToTiffConverter(settings)
	
	describe("Standard files to tif") {
	  it("can convert txt, png, jpg, jpeg, gif, bmp, pdf, ps") {
	    standardFilesTiffConverter.formats() should (
	        contain("txt")
	        and contain("png")
	        and contain("jpg")
	        and contain("jpeg")
	        and contain("gif")
	        and contain("bmp")
	        and contain("pdf")
	        and contain("ps")
	        and have length(8))
	  }
	  
	  it("converts a txt to a tif") {
	    convertTest("txt")
	  }
	  
	  it("converts a png to a tif") {
	    convertTest("png")
	  }
	  
	  it("converts a jpg to a tif") {
	    convertTest("jpg")
	  }
	  
	  it("converts a jpeg to a tif") {
	    convertTest("jpeg")
	  }
	  
	  it("converts a gif to a tif") {
	    convertTest("gif")
	  }
	  
	  it("converts bmp to a tif") {
	    convertTest("bmp")
	  }
	  
	  it("converts a pdf to a tif") {
	    convertTest("pdf")
	  }
	  
	  it("converts a ps to a tif") {
	    convertTest("ps")
	  }
	}
	
	def convertTest(extension: String) {
		inSequence {
	      settings expects 'tempFolder returning "testFiles"
	    }
	    
	    val file = new File(currentPath + "/testFiles/test." + extension)
	    val convertedFile = standardFilesTiffConverter.convert(file)
	    
	    assert(convertedFile.exists())
	    convertedFile.delete()
	}
}