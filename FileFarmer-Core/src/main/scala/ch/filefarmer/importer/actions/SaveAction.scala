package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.repositories.IArchiveFileRepository
import ch.filefarmer.logging.Logging
import com.google.inject.Inject

/**
 * saves the file into the database
 * 
 * @constructor creates a new save action
 * @param importFileRepository the file repository to use
 */
class SaveAction@Inject()(val importFileRepository: IArchiveFileRepository) extends IAction with Logging {
	
	/**
	 * @see IAction
	 */
	def execute(file: ArchiveFile): Boolean = {
	  logger.info("started save action")
	  
	  if(importFileRepository.addFile(file)) {
		  logger.info("Saved file to database")
		  true
	  } else {
		  logger.error("Couldn't save file to database")
		  false
	  }
	}
}