package ch.filefarmer.tests.labelings
import ch.filefarmer.tests.BaseTestClass
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.workflows.labelings.LabelTester
import ch.filefarmer.workflows.labelings.Label
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.poso.Archive
import ch.filefarmer.workflows.labelings.LabelDefinition

@RunWith(classOf[JUnitRunner])
class LabelTesterTests extends BaseTestClass {
	describe("the label tester") {
		it("should return the correct label") {
			val t = new LabelTester()
			Label.labelTester = t
			
			Label archive 'test2
			Label archive 'test where(file => false)
			val c = Label archive 'test where(file => true)
			
			val a = new ArchiveFile()
			a updateArchive new Archive(identity = "test", name = "test", fields = collection.mutable.Set[String]())
			val d = t.testFile(a)
			
			d.get should be(c)
		}
		it("should return none if no correct label is found") {
			val t = new LabelTester()
			Label.labelTester = t
			
			Label archive 'test2
			
			val a = new ArchiveFile()
			a updateArchive new Archive(identity = "test", name = "test", fields = collection.mutable.Set[String]())
			val d = t.testFile(a)
			
			d should be(None)
		}
		it("should return the first one if multiple labels match") {
			val t = new LabelTester()
			Label.labelTester = t
			
			val c = Label archive 'test where(file => true)
			Label archive 'test where(file => true)
			
			val a = new ArchiveFile()
			a updateArchive new Archive(identity = "test", name = "test", fields = collection.mutable.Set[String]())
			val d = t.testFile(a)
			
			d.get should be(c)
		}
		it("should return one even if the letter case don't match") {
			val t = new LabelTester()
			Label.labelTester = t
			
			val c = Label archive 'tEsT where(file => true)
			
			val a = new ArchiveFile()
			a updateArchive new Archive(identity = "test", name = "test", fields = collection.mutable.Set[String]())
			val d = t.testFile(a)
			
			d.get should be(c)
		}
	}
}