package ch.filefarmer.poso
import org.bson.types.ObjectId
import com.novus.salat.annotations._
import java.security.MessageDigest

case class User(@Key("_id")val id:ObjectId = new ObjectId(),
    			val salt: String = new String(MessageDigest.getInstance("SHA").digest((new ObjectId().toString().getBytes()))),
				var userName: String,
				@Key("encryptedPassword")var _password: String = "",
				groups: scala.collection.mutable.Set[String] = scala.collection.mutable.Set[String]()) {
	
	def password = _password
	
	def password_=(p:String) = {
		_password = new String(MessageDigest.getInstance("SHA").digest((p + salt).getBytes))
	}
}