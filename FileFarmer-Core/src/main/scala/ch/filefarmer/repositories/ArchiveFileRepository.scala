package ch.filefarmer.repositories
import ch.filefarmer.poso.ArchiveFile
import com.google.inject.Inject
import ch.filefarmer.database.connection.IConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.MongoException
import com.mongodb.casbah.gridfs.GridFS
import java.io.FileInputStream
import com.mongodb.casbah.Implicits._
import ch.filefarmer.logging.Logging
import com.novus.salat._
import com.novus.salat.global._
import ch.filefarmer.poso.ArchiveFile
import com.mongodb.casbah.commons.MongoDBList
import org.bson.types.ObjectId
import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import javax.imageio.ImageIO


/**
 * implementation to store a import file in a mongo db
 * 
 * @constructor creates a new repository
 * @param conn the connection to use
 */
class ArchiveFileRepository@Inject()(val conn: IConnection, val workflowRepository: IWorkflowRepository) extends IArchiveFileRepository with Logging {
	private val filesCollection = conn.connection()("files")
	private val gridFs = GridFS(conn.connection)
	
	/**
	 * @see IImportFileRepository
	 */
	def addFile(importFile: ArchiveFile): Boolean = {
		try {
			val obj = grater[ArchiveFile].asDBObject(importFile)
			
			val workflowBuilder = MongoDBList.newBuilder
			importFile.workflows.foreach(w => {
				workflowRepository.addWorkflow(w)
				workflowBuilder += w.id
			})
			obj += "workflows" -> workflowBuilder.result
			
			filesCollection += obj
			
			//save the tiff File
			val gridFS = GridFS(conn.connection())
			val tiffFileStream = new FileInputStream(importFile.tiffFile)
			
			gridFS(tiffFileStream) { fh =>
		  		fh.filename = importFile.id.toString
		  		fh("fileId") = importFile.id
		  		fh.contentType = "tif"
			}
			
			//save the original file
			val originalFileStream = new FileInputStream(importFile.originalFile)
			
			gridFS(importFile.originalFile) { fh =>
				fh.filename = importFile.fileName
				fh("fileId") = importFile.id
				fh.contentType = importFile.getExtension
			}
			
			//save the workflows
			
			return true
		} catch {
		  	case ex: MongoException => {
		  	  logger.info("An exception occurred in ImportFileRepository on addFile: " + ex.getStackTrace())
		  	  return false
		  	}
		  	case ex: Exception => {
		  		logger.error("Error", ex)
		  		return false
		  	}
		}
	}
	
	def updateFile(file: ArchiveFile): Boolean = {
		val obj = grater[ArchiveFile].asDBObject(file)
		val query = MongoDBObject("_id" -> file.id)
		
		val res = filesCollection.update(query, obj)
		if(res.getError() == null) {
			true
		} else {
			false
		}
	}
	
	def getFile(id:String):Option[ArchiveFile] = {
		val q = MongoDBObject("_id" -> new ObjectId(id))
		filesCollection.findOne(q) match {
		  	case Some(file) => Option(grater[ArchiveFile].asObject(file))
		  	case None => None
		}	
	}
	
	def getImageOfFile(id:String):Option[BufferedImage] = {
		
		val file = gridFs.findOne(MongoDBObject("fileId" -> new ObjectId(id), "contentType" -> "tif"))
		if(file.isDefined) {
			Some(ImageIO.read(file.get.inputStream))
		} else {
			None
		}
	}
	
	def getFilesForArchive(identity:String, numberOfFiles: Int = 50, skip: Int = 0, sort: Map[String, Int] = Map[String, Int]("fileName" -> 1)): Set[ArchiveFile] = {
		val q = MongoDBObject("archiveIdentity" -> identity)
		var coll = filesCollection.find(q).sort(sort)
		
		if(numberOfFiles > 0) {
			coll = coll.limit(numberOfFiles)
		}
		
		coll.skip(skip).map(grater[ArchiveFile].asObject(_)).toSet
	}
	
	def getNumberOfFilesForArchive(identity:String) = {
		val q = MongoDBObject("archiveIdentity" -> identity)
		
		filesCollection.find(q).count(_ != null)
	}
}