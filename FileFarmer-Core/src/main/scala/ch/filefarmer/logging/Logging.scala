package ch.filefarmer.logging
import org.apache.log4j.Logger
import org.apache.log4j.xml.DOMConfigurator

/**
 * trait to mixin into classes which need to use logging features
 */
trait Logging {
  DOMConfigurator.configure("log4j.xml")
  
  val className = this.getClass().getName
  lazy val logger = Logger.getLogger(className)
}