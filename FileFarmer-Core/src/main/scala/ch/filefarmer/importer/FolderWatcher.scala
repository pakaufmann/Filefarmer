package ch.filefarmer.importer

import com.google.inject.assistedinject.Assisted
import com.google.inject.Inject
import akka.actor.ActorRef
import collection.JavaConversions._
import com.google.inject.name.Named
import ch.filefarmer.poso.ArchiveFile
import ch.filefarmer.logging.Logging
import scala.collection.mutable.ListBuffer
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.WatchEvent
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.nio.file.WatchEvent.Kind
import java.nio.file.StandardWatchEventKinds.ENTRY_CREATE
import java.nio.file.StandardWatchEventKinds.ENTRY_DELETE
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.ClosedWatchServiceException

/**
 * a watcher implementation.
 * watches a folder and loads all files which get into this folder
 * 
 * @constructor creates a new folder watcher
 * @param dispatchDistributor the dispatch distributor to use
 * @param watchOn the folder name to watch on
 */
class FolderWatcher @Inject()(@Named("dispatchDistributor")val dispatchDistributor: ActorRef, @Assisted()val watchOn: String) extends IWatcher with Logging {
  val watcher = FileSystems.getDefault().newWatchService()
  
  override def run() = {
    logger.info("started folder watcher on folder: " + watchOn)
    
    val kinds = new ListBuffer[Kind[_]]()
    kinds += ENTRY_CREATE
    
    val watchKey = Paths.get(watchOn).register(watcher, kinds.toArray[Kind[_]])
    
    var isValid = true
    while(isValid) {
      try {
    	  var key = watcher.take()
      
	      for(event <- asScalaBuffer(key.pollEvents())) {
	        val ev = event.asInstanceOf[WatchEvent[Path]]
	        val file = Paths.get(watchOn).resolve(ev.context()).toFile()
	        
	        logger.info("got file: " + file.getCanonicalPath() + " in folder " + watchOn)
	        dispatchDistributor ! new ArchiveFile(_originalFile = file, creator = "none")
	      }
	      
	      if(!key.reset()) {
	        isValid = false
	      }
	  } catch {
	  	  case ex: ClosedWatchServiceException => {
	  	    isValid = false
	  	    logger.info("Closed folder watcher")
	  	  }
	  }
    }
  }
  
  def stopWatcher() = {
	  watcher.close()
	  logger.info("Stopped FolderWatcher on " + watchOn)
  }
}