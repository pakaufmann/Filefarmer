package ch.filefarmer.tests.repositories
import ch.filefarmer.repositories.UserRepository
import com.mongodb.casbah.commons.MongoDBObject
import ch.filefarmer.poso.User
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.repositories.DuplicateException
import ch.filefarmer.repositories.ArgumentInvalidException

@RunWith(classOf[JUnitRunner])
class UserRepositoryTests extends BaseRepositoryTestClass {
  
	describe("the user repository") {
		it("should add a user") {
			val user = new User(userName = "Username", _password = "password")
		  
			conn stubs 'connection returning mongoDB
			val rep  = new UserRepository(conn)
			
			rep.addUser(user)
			
			val q = MongoDBObject("_id" -> user.id)
			
			mongoDB("users").findOne(q) should be('defined)
		}
		it("should throw an exception if the user already exists") {
			val user = new User(userName = "user", _password = "password")
		  
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			rep.addUser(user)
			
			val thrown = evaluating { rep.addUser(user) } should produce[DuplicateException]
			thrown.getMessage should equal ("User already exists")
		}
		it("should throw an argument invalid exception if the user is empty") {
			val user = new User(userName = "", _password = "pw")
			
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			val thrown = evaluating { rep.addUser(user) } should produce[ArgumentInvalidException]
			thrown.getMessage should equal ("Username or password is empty")
		}
		it("should throw an argument invalid exception if the password is empty") {
			val user = new User(userName = "user", _password = "")
			
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			val thrown = evaluating { rep.addUser(user) } should produce[ArgumentInvalidException]
			thrown.getMessage should equal ("Username or password is empty")
		}
		it("should return true if the user and the password match") {
			val user = new User(userName = "userCorrect")
			user.password = "correct"
			
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			rep.addUser(user)
			rep.validLogin("userCorrect", "correct") should be(true)
			
		}
		it("should return false if the user and the password don't match (user wrong)") {
			val user = new User(userName = "userFalse")
			user.password = "correct"
			  
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			rep.addUser(user)
			rep.validLogin("userFalse2", "correct") should be(false)
		}
		it("should return false if the user and the password don't match (password wrong)") {
			val user = new User(userName = "userFalse2")
			user.password = "passwordFalse"
			  
			conn stubs 'connection returning mongoDB
			val rep = new UserRepository(conn)
			
			rep.addUser(user)
			rep.validLogin("userFalse2", "passwordFals") should be(false)
		}
	}
}