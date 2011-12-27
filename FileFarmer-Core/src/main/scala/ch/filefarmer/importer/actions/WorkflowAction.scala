package ch.filefarmer.importer.actions
import ch.filefarmer.workflows.IWorkflowTester
import ch.filefarmer.logging.Logging
import com.google.inject.Inject
import ch.filefarmer.poso.ArchiveFile

class WorkflowAction@Inject()(val workflowTester:IWorkflowTester) extends IAction with Logging {
	/**
	 * @see IAction
	 */
	def execute(file:ArchiveFile) = {
		logger.info("started workflow action")
		workflowTester.getWorkflows(file, true).foreach(w => {
			file.workflows += w
			w.archiveFileId = file.id
		})
		
		true
	}
}