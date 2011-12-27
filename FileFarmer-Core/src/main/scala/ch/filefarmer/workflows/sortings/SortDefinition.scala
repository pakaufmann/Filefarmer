package ch.filefarmer.workflows.sortings
import ch.filefarmer.poso.ArchiveFile
import com.google.inject.Inject

/**
 * class to create a new sorting definition
 * 
 * @constructor creates a new SortingDefinition for the given archive
 * @param archiveName the name of the archive
 */

//TODO protected[ch.filefarmer]
class SortDefinition(val archiveName:Symbol, private val sortTester:ISortTester) {
	protected var checkFunction: ArchiveFile => Boolean = (file => true)
	
	sortTester += this
	
	/**
	 * function to provide the validation for this sorting definition
	 * 
	 * @param check the function to check if the file is ok or not
	 */
	def where(check: ArchiveFile => Boolean) = {
	  	checkFunction = check
	  	this
	}
	
	/**
	 * tests this sort definition against the given 
	 * 
	 * @param file the file to check against 
	 * @return true if the file should be sorted here, else false
	 */
	private[filefarmer] def checkFile(file:ArchiveFile) = {
		checkFunction(file)
	}
}

trait ISort {
  
}

/**
 * provides the method to create a new Sorting Definition in the following format
 * <pre>
 * <code>
 * Sort to 'archiveName where {
 *   file.text.contains("text") && file.text.containts("another text")
 * }
 * </code>
 * </pre>
 */
object Sort extends ISort {
	@Inject var sortTester:ISortTester = null
  
	def to (name:Symbol) = {
		new SortDefinition(name, sortTester)
	}
}