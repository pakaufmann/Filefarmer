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

object ArchiveController extends Controller {
	implicit object ArchiveFileSetFormat extends Format[Set[ArchiveFile]] {
		def reads(json: JsValue): Set[ArchiveFile] = Set()
		def writes(s: Set[ArchiveFile]): JsValue = {
			JsObject(Map("aaData" ->
			JsArray(
			    s.toList.map(f => JsArray(List(JsString(f.fileName), JsString(f.fullText), JsString(f.creator))))
			)))
		}
	}
  
	@Inject var archiveRepository: IArchiveRepository = null
	@Inject var archiveFileRepository: IArchiveFileRepository = null
	@Inject var settings: ISettings = null
	
	def index() = Action {
		val files = archiveFileRepository.getFilesForArchive(settings.defaultArchive.name)
		val archives = archiveRepository.getArchiveTree()
		
		Ok(views.html.archive.index(archives = archives, fileSet = files, selectedArchive = settings.defaultArchive.name))
	}
	
	def getFilesForArchive(identity:String) = Action { implicit request =>
	  	Ok(toJson(archiveFileRepository.getFilesForArchive(identity)))	
	}
}