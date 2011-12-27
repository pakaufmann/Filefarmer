package ch.filefarmer.importer
import java.io.File
import scala.xml.XML
import com.google.inject.Inject
import ch.filefarmer.settings.ISettings
import ch.filefarmer.logging.Logging

/**
 * loads all watchers from the config and starts them
 * 
 * @constructor creates a new watch folder
 * @param watcherMap a map with all watchers to create
 * @param settings the system settings
 */
class WatchLoader @Inject()(val watcherMap: java.util.Map[String, IWatcherFactory], val settings: ISettings) extends IWatchLoader with Logging {
	
	/**
	 * @see IWatchLoader
	 */
	def load() = {
	    for(val watcherSetting <- settings.watchers) {
	      val watcherFactory = watcherMap.get(watcherSetting.watcherType)
	      val watcher = watcherFactory.create(watcherSetting.watchOn)
	      watcher.startWatcher()
	    }
	}
}