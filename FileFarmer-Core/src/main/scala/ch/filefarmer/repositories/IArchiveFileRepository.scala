package ch.filefarmer.repositories
import ch.filefarmer.poso.ArchiveFile

/**
 * trait for the import file repository
 */
trait IArchiveFileRepository {
	/**
	 * adds a file to the database
	 * 
	 * @param file the file to import
	 * @return true if success, else false
	 */
	def addFile(file: ArchiveFile): Boolean
	
	/**
	 * gets a file by it's id
	 * 
	 * @param the id to get
	 * @return a file if one is found, else nothing
	 */
	def getFile(id:String):Option[ArchiveFile]
	
	def getFilesForArchive(identity:String, numberOfFiles: Int = 50): Set[ArchiveFile]
}