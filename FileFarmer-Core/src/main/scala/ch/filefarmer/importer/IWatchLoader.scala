package ch.filefarmer.importer

/**
 * trait for the watch loaders
 */
trait IWatchLoader {
	/**
	 * loads all watchers
	 */
	def load()
}