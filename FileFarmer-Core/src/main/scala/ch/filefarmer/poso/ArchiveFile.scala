package ch.filefarmer.poso
import java.io.File
import scala.collection.mutable.ListBuffer
import com.novus.salat.annotations._
import org.bson.types.ObjectId
import java.util.Date

/**
 * a file from the archive
 */
case class ArchiveFile(@Key("_id")val id: ObjectId = new ObjectId(),
    				   @Ignore var _originalFile: File = null,
    				   @Ignore var tiffFile: File = null,
    				   var fileName:String = "",
    				   var fullText: String = "",
    				   var creator: String = "",
    				   @Key("archiveIdentity")var _archiveIdentity: String = "",
    				   @Ignore var workflows:ListBuffer[Workflow] = ListBuffer[Workflow](),
    				   private var _fields:collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()) {
	
	if(_originalFile != null) {
		fileName = _originalFile.getName
	}
	
	def originalFile = _originalFile
	def originalFile_=(file:File) = {
	  	fileName = file.getName
		_originalFile = file
	}
	
	def archiveIdentity = _archiveIdentity
	def fields = _fields
	
	def updateArchive(archive:Archive) = {
		_archiveIdentity = archive.identity
		val newFields = collection.mutable.Map((archive.fields zip Stream.continually("") toMap).toSeq: _*).map(f => f._1.identity -> f._2);
		
		_fields.foreach((f) => {
			if(newFields.contains(f._1)) {
				newFields += f._1 -> f._2
			}
		})
		_fields = newFields
	}
	
	def getExtension() = {
		val lastDot = fileName.lastIndexOf(".")
		fileName.substring(lastDot + 1)
	}
	
	def insertDate() = {
		new Date(id.getTime)
	}
}