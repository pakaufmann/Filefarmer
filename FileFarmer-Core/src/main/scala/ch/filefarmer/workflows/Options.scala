package ch.filefarmer.workflows

trait IOption {
	def changeOption(workflowDef: WorkflowDefinition)
}

class StartingOption() extends IOption {
	def changeOption(workflowDef: WorkflowDefinition) {
	  workflowDef.starting =true
	}
}

object Start {
	def apply() = {
		new StartingOption()
	}
}