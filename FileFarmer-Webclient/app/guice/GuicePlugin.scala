package guice
import play.api.Plugin
import com.google.inject.Guice
import com.google.inject.Injector
import ch.filefarmer.RepositoryModule

class GuicePlugin(app: play.api.Application) extends Plugin {
	var injector: Injector = null
	override def onStart() {
	 	injector = Guice.createInjector(new RepositoryModule, new WebclientModule)
	}
}