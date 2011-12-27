package ch.filefarmer.tests.repositories
import ch.filefarmer.tests.BaseTestClass
import com.mongodb.casbah.MongoConnection
import ch.filefarmer.database.connection.IConnection

abstract class BaseRepositoryTestClass extends BaseTestClass {
	val mongoDB = MongoConnection("127.0.0.1", 27017).getDB("testDb")
	mongoDB.dropDatabase()
	val conn = mock[IConnection]
}