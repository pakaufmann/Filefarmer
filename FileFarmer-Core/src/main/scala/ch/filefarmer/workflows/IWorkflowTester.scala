package ch.filefarmer.workflows
import scala.collection.mutable.ListBuffer
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.poso.Workflow
import ch.filefarmer.poso.ArchiveFile

trait IWorkflowTester {
	def +=(workflowDef:WorkflowDefinition)
	
	def getWorkflows(archiveFile:ArchiveFile, onlyStarting:Boolean = false): ListBuffer[Workflow]
}