package ch.filefarmer.workflows.sortings
import ch.filefarmer.poso.ArchiveFile

/**
 * a trait for all sort testers
 */
trait ISortTester {
	val sortDefinitions = scala.collection.mutable.Set[SortDefinition]()
  
	/**
	 * adds a new sort definition to the definitions
	 * 
	 * @param sortDefinition the sort definition to add
	 */
	def +=(sortDefinition:SortDefinition) {
		sortDefinitions += sortDefinition
	}
	
	/**
	 * tests a given file against all sort definitions
	 * 
	 * @param file the file to test
	 * @return an Option[SortDefinition]
	 */
	def testFile(file:ArchiveFile):Option[SortDefinition] = {
		sortDefinitions.find(_.checkFile(file))
	}
}