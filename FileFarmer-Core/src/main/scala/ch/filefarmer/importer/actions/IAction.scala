package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile

/**
 * the action trait.
 * needs to be implemented by all actions
 */
trait IAction {
  
	/**
	 * executes the action
	 * 
	 * @param file the file to execute the action on
	 * @return true if the action was a success, else false
	 */
	def execute(file: ArchiveFile): Boolean
}