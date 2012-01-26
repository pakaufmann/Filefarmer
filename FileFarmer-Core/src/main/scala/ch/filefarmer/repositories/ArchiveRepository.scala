package ch.filefarmer.repositories
import com.google.inject.Inject
import ch.filefarmer.database.connection.IConnection
import com.mongodb.casbah.commons.MongoDBObject
import ch.filefarmer.poso._
import com.mongodb.casbah.Implicits._
import ch.filefarmer.logging.Logging
import com.novus.salat._
import com.novus.salat.global._

class ArchiveRepository@Inject()(val conn: IConnection) extends IArchiveRepository with Logging {
	private val archiveConnection = conn.connection()("archives")
  
	def getArchive(identity:String) = {
		val search = MongoDBObject("identity" -> identity)
		
		archiveConnection.findOne(search) match {
		  case Some(ret) => {
			  Option(grater[Archive].asObject(ret))
		  }
		  case None => None
		}
	}
	
	def addArchive(archive:Archive) = {
		if(archive.identity == "") {
			throw new ArgumentInvalidException("no identity added")
		}
		if(archive.name == "") {
			throw new ArgumentInvalidException("no name added")
		}
	  
		getArchive(archive.identity) match {
		  case Some(a) => {
			  logger.error("archive with identity \"" + archive.identity + "\" already exists")
			  throw new DuplicateException("archive already exists")
		  }
		  case None => {
			  val obj = grater[Archive].asDBObject(archive)
			  
			  archiveConnection += obj
		  }
		}
	}
	
	def getArchiveTree(parent:String = ""): Option[ArchiveTree] = {
		var search = MongoDBObject("parentArchiveId" -> parent)
		
		val tree = new ArchiveTree()
		
		val results = archiveConnection.find(search)
		
		if(results.count != 0) {
			for(res <- results) {
				val archive = grater[Archive].asObject(res)
				tree.archives += archive -> getArchiveTree(archive.identity)
			}
		} else {
			return None
		}
		
		Option(tree)
	}
}