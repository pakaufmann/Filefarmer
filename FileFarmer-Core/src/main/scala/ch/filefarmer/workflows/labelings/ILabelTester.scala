package ch.filefarmer.workflows.labelings
import scala.collection.mutable.ListBuffer
import ch.filefarmer.poso.ArchiveFile

trait ILabelTester {
	val definitions = ListBuffer[ILabelDefinition]()
  
	def +=(labelDef:ILabelDefinition) {
		definitions += labelDef
	}
	
	def testFile(file:ArchiveFile):Option[ILabelDefinition]
}