package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.logging.Logging
import com.google.inject.Inject
import collection.JavaConversions._
import ch.filefarmer.ocr.IOCR

/**
 * an action to start the ocr on the file
 * 
 * @constructor creates a new ocr action
 * @param o a set with all ocr engines
 */
class OCRAction @Inject()(private val o: java.util.Set[IOCR]) extends IAction with Logging {
	private val ocrEngines: scala.collection.mutable.Set[IOCR] = asScalaSet(o)
	
	/**
	 * @see IAction
     */
	def execute(file: ArchiveFile) = {
	  logger.info("Started OCR action")
	  
	  var text = ""
	  
	  ocrEngines.find(o => o.formats().contains(file.getExtension())) match {
	    case Some(o) => {
	      logger.info("Use specialized ocr engine")
	      text = o.doOCR(file)
	    }
	    case None => {
	      logger.info("Use standard ocr engine")
	      val standard = ocrEngines.find(o => o.formats().contains("standard")).get
	      text = standard.doOCR(file)
	    }
	  }
	  
	  file.fullText = text
	  true
	}
}