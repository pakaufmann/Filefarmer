package ch.filefarmer.tests.poso
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import ch.filefarmer.poso.Archive

@RunWith(classOf[JUnitRunner])
class ArchiveFileTests extends BaseTestClass {
	describe("the archive file") {
		it("should change the archive correctly") {
			val file = new ArchiveFile(_fields = collection.mutable.Map[String, String]("field1" -> "f1", "field2" -> "f2"))
			val archive = new Archive(identity = "a", name = "a", fields = collection.mutable.Set[String]("field1", "newField"))
			
			file.archive = archive
			
			file.archive should be(archive)
			file.archiveId should be (archive.id)
		}
		it("should preserve the fields if they already exist") {
			val file = new ArchiveFile(_fields = collection.mutable.Map[String, String]("field1" -> "f1", "field2" -> "f2"))
			val archive = new Archive(identity = "a", name = "a", fields = collection.mutable.Set[String]("field1", "newField"))
			
			file.archive = archive
			
			file.fields("field1") should be("f1")
			file.fields should contain key("newField")
			file.fields should not(contain key("f2"))
		}
		it("should return the correct extension") {
			val file = new ArchiveFile(originalFile = new File("test.txt"))
			
			file.getExtension() should be("txt")
		}
	}
}