package ch.filefarmer.importer

/**
 * trait for a watcher factory
 */
trait IWatcherFactory {
	
	/**
	 * creates a new watcher
	 * 
	 * @param watchOn the folder to watch on
	 * @return a new watcher
	 */
	def create(watchOn: String): IWatcher
}