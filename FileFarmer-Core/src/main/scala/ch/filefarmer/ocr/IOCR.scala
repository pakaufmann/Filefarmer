package ch.filefarmer.ocr
import ch.filefarmer.poso.ArchiveFile

/**
 * trait for all ocr engines
 */
trait IOCR {
	/**
	 * returns all formats which can be read by this ocr engine
	 * 
	 * @return a list with all formats which can be read by this engine
	 */
	def formats(): List[String]
	
	/**
	 * read the text out of the given file
	 * 
	 * @param file the file to read the text
	 * @return a string with the text
	 */
	def doOCR(file: ArchiveFile): String
}