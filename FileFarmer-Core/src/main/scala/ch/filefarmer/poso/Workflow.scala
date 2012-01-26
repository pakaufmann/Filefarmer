package ch.filefarmer.poso
import scala.collection.mutable.ListBuffer
import com.novus.salat.annotations._
import org.bson.types.ObjectId

case class Workflow(@Key("_id")val id:ObjectId = new ObjectId(),
    				val identity:String,
    				val title:String = "",
    				val text:String = "",
    				var groups:ListBuffer[String],
    				var users:ListBuffer[String],
    				var archiveFileId:ObjectId = null)