package ch.filefarmer.tests.labelings
import org.junit.runner.RunWith
import ch.filefarmer.tests.BaseTestClass
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.workflows.labelings.Label
import ch.filefarmer.workflows.labelings.ILabelTester
import ch.filefarmer.workflows.labelings.LabelDefinition
import ch.filefarmer.workflows.labelings.LabelTester
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.workflows.labelings.ILabelDefinition

@RunWith(classOf[JUnitRunner])
class LabelDefinitionTests extends BaseTestClass {
	val labelTester = new LabelTester()
	Label.labelTester = labelTester
	
	describe("the label definition") {
		it("should add the label to the tester") {
			val label:ILabelDefinition = Label archive 'test
			
			labelTester.definitions should contain(label)
		}
		it("should call the where function in checkFile") {
			var called = false
			val label = Label archive 'test where(file => {
				called = true
				true
			})
			
			val ret = label.checkFile(new ArchiveFile())
			ret should be(true)
			called should be(true)
		}
		it("should call the label funciton in checkFile") {
			var called = false
			var label = Label archive 'test label(file => {
				called = true
			})
			
			label.labelFile(new ArchiveFile())
			called should be(true)
		}
	}
}