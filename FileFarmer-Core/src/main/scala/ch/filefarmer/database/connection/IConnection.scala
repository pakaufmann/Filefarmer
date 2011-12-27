package ch.filefarmer.database.connection
import com.mongodb.casbah.MongoDB

/**
 * trait for the connections
 */
trait IConnection {
	def connection(): MongoDB
}