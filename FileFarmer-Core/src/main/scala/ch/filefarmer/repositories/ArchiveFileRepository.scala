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


/**
 * implementation to store a import file in a mongo db
 * 
 * @constructor creates a new repository
 * @param conn the connection to use
 */
class ArchiveFileRepository@Inject()(val conn: IConnection, val workflowRepository: IWorkflowRepository) extends IArchiveFileRepository with Logging {
	private val filesCollection = conn.connection()("files")
	
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
		  		fh.contentType = "tiff"
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
	
	def getFile(id:String):Option[ArchiveFile] = {
		val q = MongoDBObject("_id" -> new ObjectId(id))
		filesCollection.findOne(q) match {
		  	case Some(file) => Option(grater[ArchiveFile].asObject(file))
		  	case None => None
		}	
	}
}