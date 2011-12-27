package ch.filefarmer
import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import scala.collection.mutable.ListBuffer
import ch.filefarmer.importer.actions._
import pluginloader._
import com.google.inject.Scopes
import scala.xml.XML
import java.io.File
import importer._
import com.google.inject.multibindings.MapBinder
import com.google.inject.assistedinject.FactoryProvider
import akka.actor.Actor
import com.google.inject.name.Names
import akka.actor.ActorRef
import com.google.inject.Provides
import com.google.inject.name.Named
import ch.filefarmer.converters._
import ch.filefarmer.ocr._
import ch.filefarmer.database.connection.IConnection
import ch.filefarmer.database.connection.MongoDbConnection
import ch.filefarmer.repositories._
import ch.filefarmer.settings._
import ch.filefarmer.database.connection.MongoDbConnection
import com.google.inject.Provider
import ch.filefarmer.workflows.sortings.ISortTester
import ch.filefarmer.workflows.sortings.SortTester
import ch.filefarmer.workflows.IWorkflowTester
import ch.filefarmer.workflows.WorkflowTester
import ch.filefarmer.workflows.IWorkflowDefinition
import ch.filefarmer.workflows.WorkflowDefinition
import ch.filefarmer.workflows.sortings.ISort
import ch.filefarmer.workflows.sortings.Sort
import ch.filefarmer.workflows.labelings.ILabel
import ch.filefarmer.workflows.labelings.Label
import ch.filefarmer.workflows.labelings.ILabelTester
import ch.filefarmer.workflows.labelings.LabelTester

/**
 * the guice core module for all dependencies 
 */
class CoreModule extends AbstractModule with ICoreModule {
  private var actionList = new ListBuffer[Class[_ <: IAction]]
  private var dispatchDistributor: ActorRef = null
  
  override def configure() = {
    setupCoreContent()
    setupRepositories()
    setupPluginContent()
    bindPluginContent()
  }
  
  /**
   * binds the core content
   */
  private def setupCoreContent() = {
    bind(classOf[IPluginLoader]).to(classOf[PluginLoader]).in(Scopes.SINGLETON)
    bind(classOf[ICoreModule]).toInstance(this)
    bind(classOf[IWatchLoader]).to(classOf[WatchLoader]).in(Scopes.SINGLETON)
    bind(classOf[IDispatcher]).to(classOf[Dispatcher])
    bind(classOf[IConnection]).to(classOf[MongoDbConnection]).in(Scopes.SINGLETON)
    bind(classOf[ISettings]).toInstance(XmlSettings)
    
    bind(classOf[ISort]).toInstance(Sort)
    bind(classOf[ISortTester]).to(classOf[SortTester]).in(Scopes.SINGLETON)
    
    bind(classOf[IWorkflowDefinition]).toInstance(WorkflowDefinition)
    bind(classOf[IWorkflowTester]).to(classOf[WorkflowTester]).in(Scopes.SINGLETON)
    
    bind(classOf[ILabel]).toInstance(Label)
    bind(classOf[ILabelTester]).to(classOf[LabelTester]).in(Scopes.SINGLETON)
    
    val watcherFactoryBinder: MapBinder[String, IWatcherFactory] = MapBinder.newMapBinder(binder(),
        classOf[String],
        classOf[IWatcherFactory]
    )
    
    watcherFactoryBinder.addBinding("folder").toProvider(FactoryProvider.newFactory(classOf[IWatcherFactory], classOf[FolderWatcher]))
    
    //converters
    val converterBinder = Multibinder.newSetBinder(binder(), classOf[IConverter])
    converterBinder.addBinding().to(classOf[StandardFilesToTiffConverter])
    converterBinder.addBinding().to(classOf[TiffToTiffConverter])
    
    //ocr engines
    val ocrBinder = Multibinder.newSetBinder(binder(), classOf[IOCR])
    ocrBinder.addBinding().to(classOf[TextOCR])
    ocrBinder.addBinding().to(classOf[StandardOCR])
  }
  
  private def setupRepositories() = {
    bind(classOf[IArchiveFileRepository]).to(classOf[ArchiveFileRepository]).in(Scopes.SINGLETON)
    bind(classOf[IArchiveRepository]).to(classOf[ArchiveRepository]).in(Scopes.SINGLETON)
    bind(classOf[IWorkflowRepository]).to(classOf[WorkflowRepository]).in(Scopes.SINGLETON)
  }
  
  /**
   * provides the dispatch distributor
   */
  @Provides @Named("dispatchDistributor")
  def provideDispatchDistributor(settings: ISettings, dispatcherProvider: Provider[IDispatcher]) = {
    if(dispatchDistributor == null) {
    	dispatchDistributor = Actor.actorOf(new DispatchDistributor(settings, dispatcherProvider)).start()
    }
    dispatchDistributor
  }
  
  /**
   * sets up the content which can be extended with plugins
   */
  private def setupPluginContent() = {
    actionList += classOf[ConvertAction]
    actionList += classOf[OCRAction]
    actionList += classOf[SortingAction]
    actionList += classOf[LabelAction]
    actionList += classOf[WorkflowAction]
    actionList += classOf[SaveAction]
    actionList += classOf[CleanupAction]
  }
  
  /**
   * bind the plugins
   */
  private def bindPluginContent() = {
    val actionBinder = Multibinder.newSetBinder(binder(), classOf[IAction])
    actionList.foreach(actionClass => actionBinder.addBinding().to(actionClass))
  }
  
  /**
   * adds a new action after the given action
   */
  def addActionAfter(actionClass: Class[_ <: IAction], after: Class[_ <: IAction]) = {
    loopAction(actionClass, after, _ + 1)
  }
  
  /**
   * adds a new action in front of the given action
   */
  def addActionBefore(actionClass: Class[_ <: IAction], before: Class[_ <: IAction]) = {
    loopAction(actionClass, before, _ + 0)
  }
  
  /**
   * internal function to loop over the actions and add a new action
   */
  protected def loopAction(actionClass: Class[_ <: IAction], insertClass: Class[_ <: IAction], f: Int => Int) = {
    actionList.foreach(t => {
      if(t == insertClass) {
        actionList.insert(f(actionList.indexOf(t)), actionClass)
      }
    })
  }
}