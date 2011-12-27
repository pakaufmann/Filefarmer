package ch.filefarmer.tests.repositories
import ch.filefarmer.repositories.ArchiveRepository
import ch.filefarmer.poso.Archive
import com.mongodb.casbah.commons.MongoDBObject
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ArchiveRepositoryTests extends BaseRepositoryTestClass {
	
	val archive = new Archive(identity = "archive", name = "archive", fields = collection.mutable.Set[String]("field1", "field2"))
  
	describe("the archive repository") {
		it("should add an archive") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			
			rep.addArchive(archive)
			
			val q = MongoDBObject("_id" -> archive.id)
			
			mongoDB("archives").findOne(q) should be('defined)
		}
		it("should throw an exception if no identity is defined") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			val arch = new Archive(identity = "", name = "name", fields = collection.mutable.Set[String]())
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[Exception]
			thrown.getMessage should equal ("no identity added")
		}
		it("should throw an exception if no name is defined") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			val arch = new Archive(identity = "id", name = "", fields = collection.mutable.Set[String]())
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[Exception]
			thrown.getMessage should equal ("no name added")
		}
		it("should throw an exception if it already exists") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			val arch = new Archive(identity = "identity", name = "name", fields = collection.mutable.Set[String]())
			rep.addArchive(arch)
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[Exception]
			thrown.getMessage should equal ("archive already exists")
		}
		it("should return an archive") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveRepository(conn)
			val ret = rep.getArchive(archive.identity)
			
			ret should be('defined)
		}
	}
}