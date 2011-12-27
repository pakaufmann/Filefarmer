package ch.filefarmer.tests.importer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.tests.BaseTestClass
import ch.filefarmer.importer.WatchLoader
import ch.filefarmer.importer.IWatcherFactory
import ch.filefarmer.settings.ISettings
import scala.collection.mutable.ListBuffer
import ch.filefarmer.settings.WatcherSettings
import ch.filefarmer.importer.IWatcher

@RunWith(classOf[JUnitRunner])
class WatchLoaderTests extends BaseTestClass {
	describe("The watchloader") {
		it("should create and start the appropriate watchers") {
			val watcherFactoryMap = new java.util.HashMap[String, IWatcherFactory]()
			val watcherFactory = mock[IWatcherFactory]
			watcherFactory stubs 'toString returning "a"
			watcherFactoryMap.put("type1", watcherFactory)
			watcherFactoryMap.put("type2", watcherFactory)
			
			val watcher = mock[IWatcher]
			watcher stubs 'toString returning "b"
			
			val settings = mock[ISettings]
			
			val watcherSettings = new ListBuffer[WatcherSettings]
			watcherSettings += new WatcherSettings("type1", "watchOn1")
			watcherSettings += new WatcherSettings("type2", "watchOn2")
			
			val watchLoader = new WatchLoader(watcherFactoryMap, settings)
			
			watcherFactory stubs 'create returning watcher
			
			inSequence {
				settings expects 'watchers returning watcherSettings
				watcher expects 'startWatcher repeat 2
			}
			
			watchLoader.load()
		}
	}
}