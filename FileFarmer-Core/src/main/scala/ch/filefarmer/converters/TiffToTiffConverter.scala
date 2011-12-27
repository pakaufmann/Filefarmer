package ch.filefarmer.converters
import java.io.File

/**
 * "converts" a tiff file to a tif file
 * simply returns the input tiff to the output tif and renames it
 */
class TiffToTiffConverter extends IConverter {
  
	/**
	 * @see IConverter
	 */
	def formats() = List[String]("tiff", "tif") 
	
	/**
	 * @see IConverter
	 */
	def convert(importFile: File) = {
		val outputFile = new File(importFile.getAbsolutePath() + ".tif")
		importFile.renameTo(outputFile)
		outputFile
	}
}