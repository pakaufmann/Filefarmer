package ch.filefarmer.pluginloader

/**
 * trait for the plugins
 * needs to be implemented by all plugins
 */
trait IPlugin {
	/**
	 * configures the plugin
	 */
	def configure(loader: IPluginLoader)
}