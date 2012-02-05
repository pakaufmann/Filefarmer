package controllers
import play.api.mvc.Controller
import play.api.mvc._
import ch.filefarmer.repositories.IArchiveRepository
import com.google.inject.Inject
import ch.filefarmer.repositories.IArchiveFileRepository
import ch.filefarmer.settings.ISettings
import play.api.libs.json._
import play.api._
import ch.filefarmer.poso.ArchiveFile
import java.io.BufferedInputStream
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.spi.IIORegistry

object ArchiveController extends Controller {
	@Inject var archiveRepository: IArchiveRepository = null
	@Inject var archiveFileRepository: IArchiveFileRepository = null
	@Inject var settings: ISettings = null
  
	implicit object ArchiveFileSetFormat extends Format[(Set[ArchiveFile], Int)] {
		def reads(json: JsValue): (Set[ArchiveFile], Int) = (Set(), 0)
		def writes(s: (Set[ArchiveFile], Int)): JsValue = {
			JsObject(Map(
			"iTotalRecords" -> JsString(s._2.toString),
			"iTotalDisplayRecords" -> JsString(s._2.toString),
			"aaData" -> JsArray(
				    s._1.toList.map(
				        f => JsArray(List(JsString(f.id.toString()),
				        				  JsString(f.fileName),
				        				  JsString(f.creator),
				        				  JsString(f.insertDate.toString)
				        			 )
				        )
				    )
				)))
		}
	}
	
	def index() = Action {
		val files = archiveFileRepository.getFilesForArchive(settings.defaultArchive.name)
		val archives = archiveRepository.getArchiveTree()
		
		Ok(views.html.archive.index(archives = archives, fileSet = files, selectedArchive = settings.defaultArchive.name))
	}
	
	def getFilesForArchive() = Action { implicit request =>
	  	val archive = request.queryString("archive")(0)
	  	val numberOfFiles = request.queryString("iDisplayLength")(0).toInt
	  	val skip = request.queryString("iDisplayStart")(0).toInt
	  	val orderMap = Map(0 -> "fileName", 1 -> "creator", 2 -> "_id")
	  	val order = orderMap(request.queryString("iSortCol_0")(0).toInt)
	  	val succession = if(request.queryString("sSortDir_0")(0) == "asc") { 1 } else { -1 }
	  	
	  	val files = archiveFileRepository.getFilesForArchive(archive, numberOfFiles, skip, Map(order -> succession))
	  	val numberOfFilesInArchive = archiveFileRepository.getNumberOfFilesForArchive(archive)
	  	
	  	Ok(toJson((files, numberOfFilesInArchive)))
	}
}