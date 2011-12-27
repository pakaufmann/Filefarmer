package ch.filefarmer.workflows.labelings
import com.google.inject.Inject
import ch.filefarmer.poso.ArchiveFile

trait ILabelDefinition {
	val archiveName: String
  
	def where(check: ArchiveFile => Boolean): ILabelDefinition
	def label(label: ArchiveFile => Unit): ILabelDefinition
	def checkFile(file:ArchiveFile): Boolean
	def labelFile(file:ArchiveFile): Unit
}

class LabelDefinition(val archiveName:String, val labelTester:ILabelTester) extends ILabelDefinition {
	protected var checkFunction: ArchiveFile => Boolean = (file => true)
	protected var labelFunction: ArchiveFile => Unit = null
	
	labelTester += this
	
	def where(check: ArchiveFile => Boolean) = {
		checkFunction = check
		this
	}
	
	def label(label: ArchiveFile => Unit) = {
		labelFunction = label
		this
	}
	
	def checkFile(file:ArchiveFile) = {
		checkFunction(file)
	}
	
	def labelFile(file:ArchiveFile) {
		labelFunction(file)
	}
}

trait ILabel {
  
}

object Label extends ILabel {
	@Inject var labelTester:ILabelTester = null
	
	def archive(archiveName:Symbol) = {
		new LabelDefinition(archiveName.name, labelTester)
	}
}