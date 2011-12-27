package ch.filefarmer.settings
import scala.xml.XML
import java.io.File
import ch.filefarmer.logging.Logging

/**
 * implementation to load the settings out of an xml file
 */
object XmlSettings extends ISettings with Logging {
	private val currentDir = new File(".").getCanonicalPath()
	private val settingsXml = XML.loadFile(currentDir + "/config.xml")
	
	try {
		//load mongo parameters
		val mongoSettings = settingsXml \\ "mongo"
		val mongoHost = (mongoSettings \\ "@host").text
		val mongoPort = (mongoSettings \\ "@port").text.toInt
		val database= (mongoSettings \\ "@database").text
		
		mongoParameters = new MongoParameters(mongoHost, mongoPort, database)
		
		//load temp folder
		tempFolder = (settingsXml \\ "tempFolder" \\ "@folder").text
		
		//load default archive
		defaultArchive = Symbol((settingsXml \\ "defaultArchive" \\ "@name").text)
		
		//load watchers
	    for(val w <- settingsXml \\ "watchers" \\ "watch") {
	      val watcherType = (w \\ "@type").text
	      val watchOn = (w \\ "@watchOn").text
	      watchers += new WatcherSettings(watcherType, watchOn)
	    }
		
	    //load dispatchers
		for(val d <- settingsXml \\ "dispatchers" \\ "host") {
		  val hostName = (d \\ "@name").text
		  val port = (d \\ "@port").text.toInt
		  val numberOfDispatchers = (d \\ "@numberOfDispatchers").text.toInt
		  dispatchers += new DispatcherSettings(hostName, port, numberOfDispatchers)
		}
	} catch {
		case ex: Exception => logger.error("An exception occurred while loading the settings" + ex.getStackTrace())
	}
}