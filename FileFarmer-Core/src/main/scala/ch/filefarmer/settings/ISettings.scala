package ch.filefarmer.settings
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import com.google.inject.Injector

/**
 * trait for the settings
 */
trait ISettings {
	/**
	 * the watchers which need to be loaded
	 */
	val watchers = new ListBuffer[WatcherSettings]
	
	/**
	 * the dispatchers which need to be loaded
	 */
	val dispatchers = new ListBuffer[DispatcherSettings]
	
	/**
	 * the parameters for the mongo db
	 */
	var mongoParameters: MongoParameters = null
	
	/**
	 * the temp folder to be used
	 */
	var tempFolder = ""
	
	/**
	 * the default archive to be used
	 */
	var defaultArchive:Symbol = Symbol("")
}