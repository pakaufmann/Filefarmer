package ch.filefarmer.ocr
import ch.filefarmer.poso.ArchiveFile

/**
 * ocr for simple text files
 * reads the text directly out of the text file
 */
class TextOCR extends IOCR {
	/**
	 * @see IOCR
	 */
	def formats() = List[String]("txt")
	
	/**
	 * @see IOCR
	 */
	def doOCR(file: ArchiveFile) = {
	  val source = scala.io.Source.fromFile(file.originalFile)
	  val lines = source.mkString
	  source.close
	  lines
	}
}