package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.i18n.Messages
import ch.filefarmer.repositories.IUserRepository
import com.google.inject.Inject

object LoginController extends Controller {
	@Inject var userRepository: IUserRepository = null
	
	val loginForm = Form(
		of(
			"username" -> of[String].verifying(required),
			"password" -> of[String].verifying(required)
		) verifying(Messages("invalid_login"), (f) => {
			userRepository.validLogin(f._1, f._2)
		})
	)
	
	def index = Action {
		Ok(views.html.login.index(userForm = loginForm))
	}
	
	def login = Action { implicit reqest =>
		loginForm.bindFromRequest.fold(
		    f => Ok(views.html.login.index(userForm = f)),
		    v => Redirect(routes.ArchiveController.index())
		)
	}
}