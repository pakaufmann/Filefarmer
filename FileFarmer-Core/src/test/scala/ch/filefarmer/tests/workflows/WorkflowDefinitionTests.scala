package ch.filefarmer.tests.workflows
import ch.filefarmer.tests.BaseTestClass
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.workflows._
import ch.filefarmer.poso.ArchiveFile

@RunWith(classOf[JUnitRunner])
class WorkflowDefinitionTests extends BaseTestClass {
	describe("the workflow definition") {
		it("should create a workflow with the same name") {
			val workflow = WorkflowDefinition('test)
			
			workflow.identity.name should be("test")
		}
		
		it("should add a start option") {
			val workflow = WorkflowDefinition('test)
			workflow.starting should be(false)
			workflow is Start()
			workflow.starting should be(true)
		}
		it("should add continues workflow") {
			val workflow = WorkflowDefinition('test) continues 'test1 continues 'test2
			
			workflow.previousWorkflows should contain('test1)
			workflow.previousWorkflows should contain('test2)
			workflow.previousWorkflows should have length(2)
		}
		it("should assign groups") {
			val workflow = WorkflowDefinition('test) assign Group('group1) and Group('group2)
		  
			workflow.groups should contain("group1")
			workflow.groups should contain("group2")
			workflow.groups should have length(2)
		}
		it("should assign users") {
			val workflow = WorkflowDefinition('test) assign User('user1) and User('user2)
			
			workflow.users should contain("user1")
			workflow.users should contain("user2")
			workflow.users should have length(2)
		}
		it("should assign a where function") {
			var gotCalled = false
			val workflow = WorkflowDefinition('test) where((file) => {
				gotCalled = true
				true
			})
			
			gotCalled should be(false)
			val ret = workflow.whereFunction(new ArchiveFile())
			ret should be(true)
			gotCalled should be(true)
		}
		it("should assign a start workflow function") {
			var gotCalled = false
			val workflow = WorkflowDefinition('test) atStart((file) => {
				gotCalled = true
			})
			
			gotCalled should be(false)
			workflow.startFunction(new ArchiveFile())
			gotCalled should be(true)
		}
		it("should assign a end workflow function") {
			var gotCalled = false
			val workflow = WorkflowDefinition('test) atEnd((file) => {
				gotCalled = true
			})
			
			gotCalled should be(false)
			workflow.endFunction(new ArchiveFile())
			gotCalled should be(true)
		}
		it("should assign a reissue function") {
			var gotCalled = false
			val workflow = WorkflowDefinition('test) reissueIf((file) => {
				gotCalled = true
				true
			})
			
			gotCalled should be(false)
			val ret = workflow.reissueFunction(new ArchiveFile())
			ret should be(true)
			gotCalled should be(true)
		}
		it("should assign the title to the definition") {
			var workflow = WorkflowDefinition('test) title "the title"
			
			workflow._title should be("the title")
		}
		it("should assign the text to the definition") {
			var workflow = WorkflowDefinition('test) text "the text"
			
			workflow._text should be("the text")
		}
		it("should create a new workflow out of the definition") {
			val workflowDef = WorkflowDefinition('test) assign Group('group1) and User('user1) title "title" text "text"
			val workflow = workflowDef.createWorkflow()
			
			workflow.identity should be("test")
			workflow.groups should contain("group1")
			workflow.groups should have length(1)
			workflow.users should contain("user1")
			workflow.users should have length(1)
			workflow.title should be("title")
			workflow.text should be("text")
		}
	}
}