package ch.filefarmer.repositories
import ch.filefarmer.poso.ArchiveFile
import java.awt.image.BufferedImage

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
	 * updates the file in the database
	 * 
	 * @param file the file to update
	 * @retunr true if success, else false
	 */
	def updateFile(file: ArchiveFile): Boolean
	
	/**
	 * gets a file by it's id
	 * 
	 * @param the id to get
	 * @return a file if one is found, else nothing
	 */
	def getFile(id:String):Option[ArchiveFile]
	
	/**
	 * returns a list of files for the given archive
	 * 
	 * @param identity the identity of the archive
	 * @param numberOfFiles the number of files to return
	 * @param skip the number of files to skip
	 * @param sort a map with the sortings
	 */
	def getFilesForArchive(identity:String, numberOfFiles: Int = 50, skip: Int = 0, sort: Map[String, Int] = Map[String, Int]("fileName" -> 1)): Set[ArchiveFile]
	
	/**
	 * returns the actual number of files in this archive
	 * 
	 * @param identity the identity of the archive
	 */
	def getNumberOfFilesForArchive(identity:String): Int
	
	/**
	 * returns the image for a file
	 * 
	 * @param the id of the file
	 */
	def getImageOfFile(id:String):Option[BufferedImage]
}