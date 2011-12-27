package ch.filefarmer.converters
import java.io.File

/**
 * interface for the converters
 * each converter should convert the import file into a tiff file
 */
trait IConverter {
	/**
	 * the formats which are accepted
	 * 
	 * @returns a list with the formats
	 */
	def formats(): List[String]
	
	/**
	 * converts the file
	 * 
	 * @returns the converted file
	 */
	def convert(importFile: File): File
}