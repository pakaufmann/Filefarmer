package ch.filefarmer.repositories
import ch.filefarmer.poso.Workflow
import ch.filefarmer.database.connection.IConnection
import com.novus.salat._
import com.novus.salat.global._
import com.google.inject.Inject

class WorkflowRepository@Inject()(val conn: IConnection) extends IWorkflowRepository {
	private val workflowCollection = conn.connection()("workflows")
	
	def addWorkflow(workflow:Workflow) {
		val obj = grater[Workflow].asDBObject(workflow)
		workflowCollection += obj
	}
}