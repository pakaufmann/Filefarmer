package ch.filefarmer.tests.repositories
import ch.filefarmer.repositories.ArchiveRepository
import ch.filefarmer.poso.Archive
import com.mongodb.casbah.commons.MongoDBObject
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.repositories._

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
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[ArgumentInvalidException]
			thrown.getMessage should equal ("no identity added")
		}
		it("should throw an exception if no name is defined") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			val arch = new Archive(identity = "id", name = "", fields = collection.mutable.Set[String]())
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[ArgumentInvalidException]
			thrown.getMessage should equal ("no name added")
		}
		it("should throw an exception if it already exists") {
			conn stubs 'connection returning mongoDB
			val rep  = new ArchiveRepository(conn)
			val arch = new Archive(identity = "identity", name = "name", fields = collection.mutable.Set[String]())
			rep.addArchive(arch)
			
			val thrown = evaluating { rep.addArchive(arch) } should produce[DuplicateException]
			thrown.getMessage should equal ("archive already exists")
		}
		it("should return an archive") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveRepository(conn)
			val ret = rep.getArchive(archive.identity)
			
			ret should be('defined)
		}
		it("should return a tree of archives") {
			conn stubs 'connection returning mongoDB
			
			mongoDB("archives").remove(MongoDBObject())
			
			val rep = new ArchiveRepository(conn)
			rep.addArchive(new Archive(identity = "1", name = "1"))
			rep.addArchive(new Archive(identity = "1.1", name = "1.1", parentArchiveId = "1"))
			rep.addArchive(new Archive(identity = "1.2", name = "1.2", parentArchiveId = "1"))
			rep.addArchive(new Archive(identity = "1.3", name = "1.3", parentArchiveId = "1"))
			rep.addArchive(new Archive(identity = "2", name = "2"))
			rep.addArchive(new Archive(identity = "2.1", name = "2.2", parentArchiveId = "2"))
			rep.addArchive(new Archive(identity = "2.1.1", name = "2.1.1", parentArchiveId = "2.1"))
			
			val ret = rep.getArchiveTree()
			
			ret should be('defined)
			val tree = ret.get
			tree.count should be(2)
			tree("1") should be('defined)
			
			val sub1 = tree("1").get._2
			sub1 should be('defined)
			val subtree1 = sub1.get
			subtree1.count should be(3)
			subtree1("1.1").get._1.identity should be("1.1")
			subtree1("1.2").get._1.identity should be("1.2")
			subtree1("1.3").get._1.identity should be("1.3")
			
			tree("2") should be('defined)
			val sub2 = tree("2").get._2
			sub2 should be('defined)
			val subtree2 = sub2.get
			subtree2.count should be(1)
			subtree2("2.1").get._1.identity should be("2.1")
			
			val sub21  = subtree2("2.1").get._2
			sub21 should be('defined)
			val subtree21 = sub21.get
			subtree21.count should be(1)
			subtree21("2.1.1").get._1.identity should be("2.1.1")
			
			val sub211 = subtree21("2.1.1").get._2
			sub211 should be(None)
		}
	}
}