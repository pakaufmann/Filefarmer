package ch.filefarmer
import pluginloader.IPluginLoader
import importer.IWatchLoader
import com.google.inject.Guice
import importer.IDispatchDistributor
import ch.filefarmer.settings.ISettings

object Main {
  def main(args: Array[String]) = {
    val injector = Guice.createInjector(new CoreModule, new RepositoryModule)
    
    val settings = injector.getInstance(classOf[ISettings])
    
    val pluginLoader = injector.getInstance(classOf[IPluginLoader])
    pluginLoader.loadPlugins()
    val watchLoader = injector.getInstance(classOf[IWatchLoader])
    watchLoader.load()
    
    //val archiveRepository = injector.getInstance(classOf[ch.filefarmer.repositories.IArchiveRepository])
    //archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "unordered", name = "Unordered", fields = collection.mutable.Set[String]("field1", "field2")))
    //archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "mainArchive", name="Main Archive"))
    //archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "subArchive1", name="Sub Archive 1", parentArchiveId = "mainArchive"))
    //archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "subArchive2", name="Sub Archive 2", parentArchiveId = "mainArchive"))
    //archiveRepository.addArchive(new ch.filefarmer.poso.Archive(identity = "subArchive11", name="Sub Archive 1.1", parentArchiveId = "subArchive1"))
    
    
    /*val archiveFileRepository = injector.getInstance(classOf[ch.filefarmer.repositories.IArchiveFileRepository])
    for(i <- 1 to 200) {
    	archiveFileRepository.addFile(new ch.filefarmer.poso.ArchiveFile(_originalFile = new java.io.File("testFiles/test.jpg"),
        															 tiffFile = new java.io.File("testFiles/test.tif"),
        															 _archiveIdentity = "unordered",
        															 fullText = "test test test test ..."))
    }*/
  }
}