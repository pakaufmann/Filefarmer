package ch.filefarmer.poso
import java.io.File
import scala.collection.mutable.ListBuffer
import com.novus.salat.annotations._
import org.bson.types.ObjectId

/**
 * a file from the archive
 */
case class ArchiveFile(@Key("_id")val id: ObjectId = new ObjectId(),
    				   @Ignore var originalFile: File = null,
    				   @Ignore var tiffFile: File = null,
    				   var fullText: String = "",
    				   @Key("archiveId")var _archiveId: ObjectId = null,
    				   @Ignore var workflows:ListBuffer[Workflow] = ListBuffer[Workflow](),
    				   private var _fields:collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()) {
	
	var _archive: Archive = null
	
	def archiveId = _archiveId
	def fields = _fields
	
	def archive = _archive
	def archive_=(archive:Archive) = {
		_archive = archive
		_archiveId = archive.id
		val newFields = collection.mutable.Map((archive.fields zip Stream.continually("") toMap).toSeq: _*);
		_fields.foreach((f) => {
			if(newFields.contains(f._1)) {
				newFields += f._1 -> f._2
			}
		})
		_fields = newFields
	}
	
	def getExtension() = {
		val fileName = originalFile.getName()
		val lastDot = fileName.lastIndexOf(".")
		fileName.substring(lastDot + 1)
	} 
	def fileName = originalFile.getName()
}