package guice
import com.google.inject.AbstractModule
import play.api.mvc.Controller
import controllers._
import ch.filefarmer.settings._
import com.google.inject.name.Names

class WebclientModule extends AbstractModule {
	override def configure() = {
		bind(classOf[Controller]).annotatedWith(Names.named("LoginController")).toInstance(LoginController)
		bind(classOf[Controller]).annotatedWith(Names.named("ArchiveController")).toInstance(ArchiveController)
		
		bind(classOf[ISettings]).toInstance(XmlSettings)
	}
}