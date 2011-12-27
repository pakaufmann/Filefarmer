package ch.filefarmer.importer
import akka.actor.TypedActor

/**
 * trait for a watcher
 */
trait IWatcher extends Thread {
	
	/**
	 * starts the watcher
	 * 
	 */
	def startWatcher() {
		start()
	}
	
	/**
	 * stops the watcher
	 */
	def stopWatcher(): Unit
}