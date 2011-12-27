package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.workflows.labelings.ILabelTester
import com.google.inject.Inject
import ch.filefarmer.logging.Logging

class LabelAction@Inject()(val labelTester:ILabelTester) extends IAction with Logging {
	def execute(file: ArchiveFile) = {
		logger.info("started label action")
		labelTester.testFile(file) match {
		  	case Some(d) => {
		  		logger.info("labeled file " + file.id)
		  		d.labelFile(file)
		  	}
		  	case None => true
		}
		
		true
	}
}