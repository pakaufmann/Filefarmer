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
import ch.filefarmer.poso.Archive

@RunWith(classOf[JUnitRunner])
class ArchiveFileRepositoryTests extends BaseRepositoryTestClass {
	val wfRep = mock[IWorkflowRepository]
  
	val f = new ArchiveFile(_originalFile = new File(currentPath + "/testFiles/test.png"),
							tiffFile = new File(currentPath + "/testFiles/test.tif"))
	
	describe("the archive file repository") {
		it("should add a file to the database") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			val ret = rep.addFile(f)
			val q = MongoDBObject("_id" -> f.id)
			val qFs = MongoDBObject("fileId" -> f.id, "contentType" -> "tif")
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
		it("should return a set of files for the given archive") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			//delete all files
			mongoDB("files").remove(MongoDBObject())
			//add files
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			
			val ret = rep.getFilesForArchive("testArchive")
			ret.count(_ == null || true) should be(5)
			ret.find(_.fullText == "text1") should be('defined)
		}
		it("should return an empty set if no files are found") {
			conn stubs 'connection returning mongoDB
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			//delete all files
			mongoDB("files").remove(MongoDBObject())
			
			val ret = rep.getFilesForArchive("testArchive")
			ret.count(_ != null) should be(0)
		}
		it("should limit the number of files if set") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			
			val ret = rep.getFilesForArchive("testArchive", 2)
			ret.count( _ != null) should be(2)
		}
		it("should order them correctly") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text3", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text2", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			
			val ret = rep.getFilesForArchive("testArchive", 3, 0, Map("fullText" -> -1))
			ret.count( _ != null) should be(3)
			ret.toList(0).fullText should be("text3")
			ret.toList(1).fullText should be("text2")
			ret.toList(2).fullText should be("text1")
		}
		it("should return all items on a negative sort") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			
			for(i <- 1 to 200) rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			val ret = rep.getFilesForArchive("testArchive", -1, 0, Map("fullText" -> -1))
			ret.count(_ != null) should be(200)
		}
		it("should skip the first items, if told so") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text3", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text2", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			
			val ret = rep.getFilesForArchive("testArchive", 2, 1, Map("fullText" -> -1))
			ret.count( _ != null) should be(2)
			ret.toList(0).fullText should be("text2")
			ret.toList(1).fullText should be("text1")
		}
		it("should return the number of files in an archive") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text3", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text2", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			rep.addFile(new ArchiveFile(_archiveIdentity = "anotherArchive", fullText = "text2", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif")))
			
			val ret = rep.getNumberOfFilesForArchive("testArchive")
			ret should be(3)
		}
		it("should return an image for the file") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			
			mongoDB("files").remove(MongoDBObject())
			val file = new ArchiveFile(_archiveIdentity = "testArchive", fullText = "text1", _originalFile = new File(currentPath + "/testFiles/test.png"), tiffFile = new File(currentPath + "/testFiles/test.tif"))
			rep.addFile(file)
			
			val ret = rep.getImageOfFile(file.id.toString())
			
			ret should be('defined)
			ret.get should not be(null)
		}
		it("should update a file correctly") {
			conn stubs 'connection returning mongoDB
			
			val rep = new ArchiveFileRepository(conn, wfRep)
			val archiveRep = new ArchiveRepository(conn)
			
			mongoDB("files").remove(MongoDBObject())
			val file = new ArchiveFile(_archiveIdentity = "testArchive",
			    					   fullText = "text1",
			    					   _originalFile = new File(currentPath + "/testFiles/test.png"),
			    					   tiffFile = new File(currentPath + "/testFiles/test.tif"),
			    					   _fields = scala.collection.mutable.Map("exist" -> "exist", "remove" -> "remove"))
			rep.addFile(file)
			val archive = new Archive(identity = "moveArchive", name = "Move Archive", fields = scala.collection.mutable.Set("exist", "new"))
			archiveRep.addArchive(archive)
			file.updateArchive(archive)
			
			val ret = rep.updateFile(file)
			
			ret should be(true)
			val updatedFileOption = rep.getFile(file.id.toString)
			updatedFileOption should be('defined)
			val updatedFile = updatedFileOption.get
			updatedFile.fields should contain key("exist")
			updatedFile.fields("exist") should be("exist")
			updatedFile.fields should contain key("new")
			updatedFile.fields("new") should be("")
			updatedFile.fields should not contain key("remove")
			updatedFile.archiveIdentity should be("moveArchive")
		}
	}
}