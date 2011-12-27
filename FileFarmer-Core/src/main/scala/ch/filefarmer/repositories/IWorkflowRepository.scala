package ch.filefarmer.repositories
import ch.filefarmer.poso.Workflow

trait IWorkflowRepository {
	def addWorkflow(workflow:Workflow)
}