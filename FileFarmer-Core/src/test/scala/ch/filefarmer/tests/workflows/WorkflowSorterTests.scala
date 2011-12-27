package ch.filefarmer.tests.workflows
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.workflows.WorkflowTester
import ch.filefarmer.workflows.WorkflowDefinition
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.workflows.Start

@RunWith(classOf[JUnitRunner])
class WorkflowSorterTests extends BaseTestClass {
	describe("the workflow sorter") {
		it("should return the correct definition") {
			val tester = new WorkflowTester()
			val def1 = WorkflowDefinition('test1) where((file) => false)
			val def2 = WorkflowDefinition('test2) where((file) => true)
			tester += def1
			tester += def2
			
			val workflows = tester.getWorkflows(new ArchiveFile())
			
			workflows should have length(1)
			workflows(0).identity should be("test2") 
		}
		it("should return multiple workflows") {
			val tester = new WorkflowTester()
			val def1 = WorkflowDefinition('test1) where((file) => true)
			val def2 = WorkflowDefinition('test2) where((file) => true)
			tester += def1
			tester += def2
			
			val workflows = tester.getWorkflows(new ArchiveFile())
			
			workflows should have length(2)
		}
		it("should use only the starting workflows if asked so") {
			val tester = new WorkflowTester()
			val def1 = WorkflowDefinition('test1) is Start() where((file) => true)
			val def2 = WorkflowDefinition('test) where((file) => true)
			tester += def1
			tester += def2
			
			val workflows = tester.getWorkflows(new ArchiveFile(), true)
			
			workflows should have length(1)
			workflows(0).identity should be("test1")
		}
	}
}