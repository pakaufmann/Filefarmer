package ch.filefarmer.tests.repositories
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.repositories.WorkflowRepository
import ch.filefarmer.poso.Workflow
import scala.collection.mutable.ListBuffer
import com.mongodb.casbah.commons.MongoDBObject

@RunWith(classOf[JUnitRunner])
class WorkflowRepositoryTests extends BaseRepositoryTestClass {
	describe("the workflow repository") {
		it("should add a workflow") {
			conn stubs 'connection returning mongoDB
			val rep = new WorkflowRepository(conn)
			val wf = new Workflow(identity = "id", groups = ListBuffer[String](), users = ListBuffer[String]())
			
			rep.addWorkflow(wf)
			val q = MongoDBObject("_id" -> wf.id)
			
			mongoDB("workflows").findOne(q) should be('defined)
		}
	}
}