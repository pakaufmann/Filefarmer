package ch.filefarmer.pluginloader
import java.io.File
import scala.xml.XML
import ch.filefarmer.logging.Logging
import java.net.URL
import scala.collection.mutable.ListBuffer
import java.net.URLClassLoader
import ch.filefarmer.CoreModule
import com.google.inject.Inject

/**
 * the plugin loader
 * gets all plugins in the plugin.xml and loads them into the system
 * 
 * @constructor creates a new plugin loader and loads all plugins
 * @param coreModule the core module to use
 */
class PluginLoader @Inject()(val coreModule: CoreModule) extends IPluginLoader with Logging {
  def loadPlugins() {
	  logger.info("Started Plugin loader")
	  
	  val plugins = new ListBuffer[IPlugin]
	  
	  val currentDir = new File(".").getCanonicalPath()
	  val pluginDir = currentDir + "/plugins"
	  
	  val pluginXml = XML.loadFile(pluginDir + "/plugins.xml")
	  var urls = new ListBuffer[URL]
	  var mainClasses = new ListBuffer[String]
	  
	  for(val plugin <- pluginXml \\ "plugin") {
	    urls += new File(pluginDir + "/" + (plugin \\ "@jar").text).toURL()
	    mainClasses += (plugin \\ "@mainClass").text
	  }
	  
	  val loader: ClassLoader = new URLClassLoader(urls.toArray)
	  mainClasses.foreach(className => {
	    try {
	    	val pluginClass = loader.loadClass(className)
	    	val plugin = pluginClass.newInstance().asInstanceOf[IPlugin]
	    	
	    	logger.info("Load plugin " + className)
		    plugin.configure(this)
		    plugins += plugin
		    logger.info("Loaded plugin " + className)
	    } catch {
	      case ex: ClassNotFoundException => {
	    	  logger.error("Could not load class " + className, ex)
	      } 
	    }
	  })
  }
}