package ch.filefarmer
import com.google.inject.AbstractModule
import ch.filefarmer.repositories._
import com.google.inject.Scopes
import ch.filefarmer.database.connection.IConnection
import ch.filefarmer.database.connection.MongoDbConnection

class RepositoryModule extends AbstractModule {
	def configure() = {
		bind(classOf[IConnection]).to(classOf[MongoDbConnection]).in(Scopes.SINGLETON)
		bind(classOf[IArchiveFileRepository]).to(classOf[ArchiveFileRepository]).in(Scopes.SINGLETON)
		bind(classOf[IArchiveRepository]).to(classOf[ArchiveRepository]).in(Scopes.SINGLETON)
    	bind(classOf[IWorkflowRepository]).to(classOf[WorkflowRepository]).in(Scopes.SINGLETON)
    	bind(classOf[IUserRepository]).to(classOf[UserRepository]).in(Scopes.SINGLETON)
	}
}