package ch.filefarmer.repositories
import com.google.inject.Inject
import ch.filefarmer.database.connection.IConnection
import com.mongodb.casbah.commons.MongoDBObject
import java.security.MessageDigest
import ch.filefarmer.poso.User
import com.novus.salat._
import com.novus.salat.global._
import com.mongodb.casbah.Implicits._
import org.bson.types.ObjectId
import ch.filefarmer.logging.Logging

class UserRepository@Inject()(val conn:IConnection) extends IUserRepository with Logging {
	private val userConnection = conn.connection()("users")
	
	def getUser(name: String): Option[User] = {
		val search = MongoDBObject("userName" -> name)
		
		userConnection.findOne(search) match {
		  	case Some(ret) => {
		  		Option(grater[User].asObject(ret))
		  	}
		  	case None => None
		}
	}
	
	def addUser(user: User): Boolean = {
		if(user.userName.trim() == "" || user.password == "") {
			throw new ArgumentInvalidException("Username or password is empty")
		}
	  
		if(getUser(user.userName).isDefined) {
			logger.error("User " + user.userName + " already exists in the database")
			throw new DuplicateException("User already exists")
		}
	  
		try {
			userConnection += grater[User].asDBObject(user)
			
			logger.info("User " + user.userName + " was added to the database")
			true
		} catch {
		  	case ex: Exception => {
		  		false
		  	}
		}
	}
	
	/**
	 * checks if the login is valid
	 * returns true if the login is valid, else false
	 * 
	 * @param user the username
	 * @param password the password (cleartext)
	 */
	def validLogin(user:String, password:String): Boolean = {
		val search = MongoDBObject("userName" -> user)
		
		userConnection.findOne(search) match {
		  case Some(ret) => {
			  val user = grater[User].asObject(ret)
			  
			  val encPassword = new String(MessageDigest.getInstance("SHA").digest((password + user.salt).getBytes()))
			  
			  if(user.password == encPassword) {
				  true
			  } else {
				  false
			  }
		  }
		  case None => false
		}
	}
}