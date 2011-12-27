package ch.filefarmer.tests.repositories
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.repositories._
import ch.filefarmer.database.connection.IConnection
import com.mongodb.casbah.MongoConnection
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId
import com.mongodb.casbah.gridfs.GridFS

@RunWith(classOf[JUnitRunner])
class ArchiveFileRepositoryTests extends BaseRepositoryTestClass {
	val wfRep = mock[IWorkflowRepository]
  
	val f = new ArchiveFile(originalFile = new File(currentPath + "/testFiles/test.png"),
							tiffFile = new File(currentPath + "/testFiles/test.tif"))
	
	describe("the archive file repository") {
		it("should add a file to the database") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			val ret = rep.addFile(f)
			val q = MongoDBObject("_id" -> f.id)
			val qFs = MongoDBObject("fileId" -> f.id, "contentType" -> "tiff")
			val qFs2 = MongoDBObject("fileId" -> f.id, "filename" -> f.fileName)
			
			ret should be(true)
			mongoDB("files").findOne(q) should be('defined)
			GridFS(mongoDB).findOne(qFs) should be('defined)
			GridFS(mongoDB).findOne(qFs2) should be('defined)
		}
		it("should return a file by its id") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			val ret = rep.getFile(f.id.toString)
			ret should be('defined)
		}
		it("should return none if nothing is found") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			val ret = rep.getFile(new ObjectId().toString)
			ret should be(None)
		}
	}
}