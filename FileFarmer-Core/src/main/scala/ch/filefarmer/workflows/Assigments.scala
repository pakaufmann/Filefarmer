package ch.filefarmer.workflows
import scala.collection.mutable.ListBuffer

trait IAssignment {
	def addAssignment(workflowDef:WorkflowDefinition)
}

class GroupAssigment(val groupName:Symbol) extends IAssignment {
	def addAssignment(workflowDef:WorkflowDefinition) {
		workflowDef.groups += groupName.name
	}
}

object Group {
	def apply(groupName:Symbol) = {
		new GroupAssigment(groupName)
	}
}

class UserAssigment(val userName:Symbol) extends IAssignment {
	def addAssignment(workflowDef:WorkflowDefinition) {
		workflowDef.users += userName.name
	}
}

object User {
	def apply(userName:Symbol) = {
		new UserAssigment(userName)
	}
}