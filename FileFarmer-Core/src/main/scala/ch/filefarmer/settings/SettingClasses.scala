package ch.filefarmer.settings

/**
 * class for a dispatcher setting
 * 
 * @param host the host to use
 * @param port the port to use
 * @param numberOfDispatchers the number of dispatchers to create
 */
case class DispatcherSettings(val host: String, val port: Int, val numberOfDispatchers: Int)

/**
 * the settings for a watcher
 * 
 * @param watcherType the type of the watcher
 * @param watchOn the folder to watch on
 */
case class WatcherSettings(val watcherType: String, val watchOn: String)

/**
 * the settings for the mongo db
 * 
 * @param host the host to connect to
 * @param port the port to use
 * @param db the database name
 */
case class MongoParameters(val host: String, val port: Integer, val db: String)