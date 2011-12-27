package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile
import com.google.inject.Inject
import ch.filefarmer.converters.IConverter
import collection.JavaConversions._
import ch.filefarmer.logging.Logging

/**
 * converts the import file into a tiff format
 * 
 * @constructor creates a new converter action
 * @param c a set with all converters
 */
class ConvertAction @Inject()(val c: java.util.Set[IConverter]) extends IAction with Logging {
	private val converters: scala.collection.mutable.Set[IConverter] = asScalaSet(c)
  
	/**
	 * @see IAction
	 */
	def execute(file: ArchiveFile) = {
	  	logger.info("Started convert action")
	    converters.find(c => c.formats().contains(file.getExtension())) match {
	      case Some(c) => {
	        file.tiffFile = c.convert(file.originalFile)
	        true
	      }
	      case None => {
	        this.logger.info("No converter found for this file")
	        false
	      }
	    }
	}
}