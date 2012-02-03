package ch.filefarmer.tests.poso
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import ch.filefarmer.poso.Archive
import java.util.Date

@RunWith(classOf[JUnitRunner])
class ArchiveFileTests extends BaseTestClass {
	describe("the archive file") {
		it("should change the archive correctly") {
			val file = new ArchiveFile(_fields = collection.mutable.Map[String, String]("field1" -> "f1", "field2" -> "f2"))
			val archive = new Archive(identity = "a", name = "a", fields = collection.mutable.Set[String]("field1", "newField"))
			
			file updateArchive archive
			
			file.archiveIdentity should be (archive.identity)
		}
		it("should preserve the fields if they already exist") {
			val file = new ArchiveFile(_fields = collection.mutable.Map[String, String]("field1" -> "f1", "field2" -> "f2"))
			val archive = new Archive(identity = "a", name = "a", fields = collection.mutable.Set[String]("field1", "newField"))
			
			file updateArchive archive
			
			file.fields("field1") should be("f1")
			file.fields should contain key("newField")
			file.fields should not(contain key("f2"))
		}
		it("should return the fileName and the correct extension") {
			val file = new ArchiveFile(_originalFile = new File("test.txt"))
			
			file.fileName should be("test.txt")
			file.getExtension() should be("txt")
		}
		it("should return the correct insertion date") {
			val file = new ArchiveFile(_originalFile = new File("test.txt"))
			
			file.insertDate should be(new Date(file.id.getTime())) 
		}
	}
}