package ch.filefarmer.workflows
import scala.collection.mutable.ListBuffer
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.poso.Workflow
import ch.filefarmer.logging.Logging
import ch.filefarmer.poso.ArchiveFile

class WorkflowTester extends IWorkflowTester with Logging {
	protected var workflowDefinitions = ListBuffer[WorkflowDefinition]()
	
	def +=(workflowDef:WorkflowDefinition) {
		logger.info("added workflow " + workflowDef.identity.name + " to the workflows")
		workflowDefinitions += workflowDef
	}
	
	def getWorkflows(archiveFile:ArchiveFile, onlyStarting:Boolean = false): ListBuffer[Workflow] = {
		workflowDefinitions
		.filter((f) => {
			if(onlyStarting) {
				if(f.starting) {
					true
				} else {
					false
				}
			} else {
				true
			}
		})
		.filter(_.whereFunction(archiveFile))
		.map(_ createWorkflow())
	}
}