package ch.filefarmer.workflows
import scala.collection.mutable.ListBuffer
import ch.filefarmer.poso.ArchiveFile
import com.google.inject.Inject
import ch.filefarmer.poso.Workflow
import ch.filefarmer.logging.Logging
import ch.filefarmer.poso.ArchiveFile
/**
 * allows to define custom workflows
 *
 * @example:
 * WorkflowDefinition('test) is Start() and Additional()
 * continues 'previous continues 'previous2
 * assign group('group1) and user('user2)
 * title "this is the title" text "this is the workflow text"
 * where((file) => {
 *		file.fullText contains "test"
 *	}) atStart((file) => {
 *	}) atEnd((file) => {
 *	}) reissueIf((file) => {
 *  })
 */
class WorkflowDefinition(val identity:Symbol, private val workflowTester:IWorkflowTester) {
	private[filefarmer] var starting = false
	private[filefarmer] var additional = false
	
	private[filefarmer] var groups = ListBuffer[String]()
	private[filefarmer] var users = ListBuffer[String]()
	
	private[filefarmer] var _text = ""
	private[filefarmer] var _title = ""
	
	private[filefarmer] var previousWorkflows: ListBuffer[Symbol] = new ListBuffer[Symbol]()
	private[filefarmer] var whereFunction: (ArchiveFile) => Boolean = (f) => false
	private[filefarmer] var startFunction: (ArchiveFile) => Unit = (f) => null
	private[filefarmer] var endFunction: (ArchiveFile) => Unit = (f) => null
	private[filefarmer] var reissueFunction: (ArchiveFile) => Boolean = (f) => true
	
	if(workflowTester != null) {
		workflowTester += this
	}
	
	def is(option:IOption) = {
		option changeOption this
		this
	}
	
	def and(option:IOption) = {
		option changeOption this
		this
	}
	
	def and(assigment:IAssignment) = {
		assigment addAssignment this
		this
	}
	
	def assign(assigment:IAssignment) = {
		assigment addAssignment this
		this
	}
	
	/**
	 * takes an identity of another workflow
	 * 
	 */
	def continues(identity:Symbol) = {
		previousWorkflows += identity
		this
	}
	
	/**
	 * defines when the workflow will get started
	 */
	def where(func: (ArchiveFile) => Boolean) = {
		whereFunction = func
		this
	}
	
	/**
	 * defines a function which will get started when the workflow starts
	 */
	def atStart(func: (ArchiveFile) => Unit) = {
		startFunction = func
		this
	}
	
	/**
	 * defines a function which will get started when the workflow ends
	 */
	def atEnd(func: (ArchiveFile) => Unit) = {
		endFunction = func
		this
	}
	
	/**
	 * defines a function which gets called at the end
	 * if the function returns true, the workflow will not end and 
	 * instead get reissued
	 */
	def reissueIf(func: (ArchiveFile) => Boolean) = {
		reissueFunction = func
		this
	}
	
	def title(t: String) = {
		_title = t
		this
	}
	
	def text(t: String) = {
		_text = t
		this
	}
	
	/**
	 * creates a workflow out of this definition
	 */
	private[filefarmer] def createWorkflow(): Workflow = {
		new Workflow(identity = identity.name, groups = groups, users = users, title = _title, text = _text)
	}
}

trait IWorkflowDefinition {
	def apply(identity:Symbol): WorkflowDefinition
}

object WorkflowDefinition extends IWorkflowDefinition {
	@Inject val workflowTester:IWorkflowTester = null
  
	def apply(identity:Symbol) = {
		new WorkflowDefinition(identity, workflowTester)
	}
}