package ch.filefarmer.repositories
import ch.filefarmer.poso.User
import org.bson.types.ObjectId

trait IUserRepository {
	def getUser(id: String): Option[User]
	def addUser(user: User): Boolean
	def validLogin(user:String, password:String): Boolean
}