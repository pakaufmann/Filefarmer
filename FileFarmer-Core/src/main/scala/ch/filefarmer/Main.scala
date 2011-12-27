package ch.filefarmer
import pluginloader.IPluginLoader
import importer.IWatchLoader
import com.google.inject.Guice
import importer.IDispatchDistributor
import ch.filefarmer.settings.ISettings

object Main {
  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new CoreModule)
    
    val settings = injector.getInstance(classOf[ISettings])
    
    val pluginLoader = injector.getInstance(classOf[IPluginLoader])
    pluginLoader.loadPlugins()
    val watchLoader = injector.getInstance(classOf[IWatchLoader])
    watchLoader.load()
    
    /*val archiveRepository = injector.getInstance(classOf[ch.filefarmer.repositories.IArchiveRepository])
    archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "unordered", name = "Unordered", fields = Set[String]("field1", "field2")))*/
  }
}