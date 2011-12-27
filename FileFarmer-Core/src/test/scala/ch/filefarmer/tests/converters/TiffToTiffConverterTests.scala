package ch.filefarmer.tests.converters
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.settings.ISettings
import ch.filefarmer.converters.TiffToTiffConverter
import java.io.File
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TiffToTiffConverterTests extends BaseTestClass {
	val tiffToTiffConverter = new TiffToTiffConverter()
	
	describe("Tiff to tiff converter") {
	  it("renames a tiff to tif") {
	    val outputFile = tiffToTiffConverter.convert(new File(currentPath + "/testFiles/test.tiff"))
	    assert(outputFile.exists())
	    outputFile.renameTo(outputFile)
	  }
	}
}