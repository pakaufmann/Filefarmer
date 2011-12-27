package ch.filefarmer.importer

import com.google.inject.Inject
import actions.IAction
import collection.JavaConversions._
import akka.dispatch.Dispatchers
import ch.filefarmer.poso.ArchiveFile
import scala.util.Random
import ch.filefarmer.logging.Logging
import com.google.inject.assistedinject.Assisted
import java.util.UUID
import java.io.File
import ch.filefarmer.settings.ISettings

/**
 * the dispatcher actor. Handles an incoming file
 * loops over all actions and stores the file in the database
 * 
 * @constructor creates a new dispatcher
 * @param a a set with all actions for this dispatcher
 * @param settings the system settings
 */
class Dispatcher @Inject()(val a: java.util.Set[IAction], private val settings: ISettings) extends IDispatcher with Logging {
  self.dispatcher = Dispatchers.newThreadBasedDispatcher(self)
  
  private val actions: scala.collection.mutable.Set[IAction] = asScalaSet(a)
  
  val id = UUID.randomUUID();
  
  /**
   * receive action to dispatch a given file
   */
  def receive = {
    case file: ArchiveFile => {
      //move the file to the temp dir and rename it to a unique name
      val dir = new File(settings.tempFolder + "/" + id + "." + file.getExtension())
      file.originalFile.renameTo(dir)
      file.originalFile = dir
      
      logger.info("received a file on dispatcher " + id)
      
      var returnValue = true
      actions.toStream.takeWhile(c => returnValue).foreach(a => returnValue = a.execute(file))
    }
  }
}