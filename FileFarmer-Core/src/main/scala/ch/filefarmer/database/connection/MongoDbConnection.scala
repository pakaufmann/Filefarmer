package ch.filefarmer.database.connection
import com.mongodb.casbah.MongoConnection
import com.google.inject.Inject
import ch.filefarmer.settings.ISettings
import ch.filefarmer.logging.Logging
import java.util.UUID

/**
 * class for the mongo db connection
 * 
 * @constructor creates a new db connection
 * @param settings the system settings
 */
class MongoDbConnection@Inject()(private val settings: ISettings) extends IConnection with Logging {
	logger.info("Connect to host: " + settings.mongoParameters.host + " on port " + settings.mongoParameters.port)
	val mongoConn = MongoConnection(settings.mongoParameters.host, settings.mongoParameters.port)
	
	/**
	 * provides the connection
	 * 
	 * @returns the connection object
	 */
	def connection() = {
	  mongoConn.getDB(settings.mongoParameters.db)
	}
}