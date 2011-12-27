package ch.filefarmer.importer.actions
import ch.filefarmer.logging.Logging
import com.google.inject.Inject
import ch.filefarmer.workflows.sortings.ISortTester
import ch.filefarmer.settings.ISettings
import ch.filefarmer.repositories.IArchiveRepository
import ch.filefarmer.poso.ArchiveFile

/**
 * an action to check in which archive the file should be sorted
 * 
 * @constructor creates a new sorting action
 * @param sortTester the sort tester to use
 * @param settings the system settings
 */
class SortingAction@Inject()(val sortTester:ISortTester, val settings: ISettings, val archiveRepository: IArchiveRepository) extends IAction with Logging {
	 
	/**
	 * @see IAction
	 */
	def execute(file:ArchiveFile) = {
		 logger.info("Started sorting action")
		 
		 var archive: String = ""
		 
		 sortTester.testFile(file) match {
		   	case Some(sorting) =>  {
		   		logger.info("sorted " + file.fileName + " to archive " + sorting.archiveName)
		   		archive = sorting.archiveName.name
		   	}
		   	case None => {
		   		logger.info("sorted " + file.fileName + " to the default archive")
		   		archive = settings.defaultArchive.name
		   	}
		 }
		 
		 archiveRepository.getArchive(archive) match {
		   case Some(archive) => {
			   file.archive = archive
			   true
		   }
		   case None => {
			   logger.error("Archive " + archive + " not found")
			   false
		   }
		 }
	 }
}