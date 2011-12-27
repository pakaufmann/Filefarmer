package ch.filefarmer.ocr
import ch.filefarmer.poso.ArchiveFile
import java.io.FileInputStream
import java.nio.channels.FileChannel.MapMode
import ch.filefarmer.logging.Logging
import java.io.File
import java.nio.file.Files
import akka.actor.Actor
import ch.filefarmer.settings.ISettings
import com.google.inject.Inject

/**
 * standard ocr engine if no specialized ocr engines are found
 */
class StandardOCR@Inject()(val settings:ISettings) extends IOCR with Logging {
	def formats() = List[String]("standard")
	
	/**
	 * @see IOCR
	 */
	def doOCR(file: ArchiveFile): String = {
		try {
		  val outputPath = settings.tempFolder + "/" + file.tiffFile.getName()
		  val outputFile = new File(outputPath + ".txt")
		  
		  var cmd = "tesseract"
		  cmd = cmd + " " + file.tiffFile.getAbsolutePath()
		  cmd = cmd + " " + outputPath
		  cmd = cmd + " -l deu"
		  
		  val process = Runtime.getRuntime exec cmd
		  val exitVal = process.waitFor()
		  val source = scala.io.Source.fromFile(outputFile)
		  source.mkString
		} catch {
		  case ex: Exception => {
		    logger.error("An error occured in the standard ocr: " + ex.getStackTrace())
		    throw ex
		  }
		}
	}
}