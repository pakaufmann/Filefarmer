package ch.filefarmer.tests.actions
import ch.filefarmer.tests.BaseTestClass
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import ch.filefarmer.importer.actions._
import ch.filefarmer.poso.ArchiveFile
import java.io.File
import java.nio.file.Files
import ch.filefarmer.converters.IConverter
import ch.filefarmer.ocr.IOCR
import ch.filefarmer.repositories.IArchiveFileRepository
import ch.filefarmer.workflows.sortings.ISortTester
import ch.filefarmer.settings.ISettings
import ch.filefarmer.workflows.sortings.SortDefinition
import ch.filefarmer.repositories.IArchiveRepository
import ch.filefarmer.poso.Archive
import ch.filefarmer.workflows.IWorkflowTester
import ch.filefarmer.poso.Workflow
import scala.collection.mutable.ListBuffer
import ch.filefarmer.workflows.sortings.Sort
import ch.filefarmer.workflows.sortings.SortTester
import ch.filefarmer.workflows.labelings._

@RunWith(classOf[JUnitRunner])
class ActionTests extends BaseTestClass {
	describe("the various actions:") {
	  it("the cleanup action should remove all files from the hard disk") {
		  val cleanupAction = new CleanupAction()
		  val file = new File(currentPath + "/testFiles/delete1.txt")
		  val tiffFile = new File(currentPath + "/testFiles/delete2.txt")
		  val testFile = new File(currentPath + "/testFiles/test.txt")
		  Files.copy(testFile.toPath, file.toPath)
		  Files.copy(testFile.toPath, tiffFile.toPath)
		  
		  assert(file.exists)
		  assert(tiffFile.exists)
		  
		  val importFile = new ArchiveFile(_originalFile = file)
		  importFile.tiffFile = tiffFile
		  
		  cleanupAction.execute(importFile)
		  
		  file.exists should be(false)
		  tiffFile.exists should be(false)
	  	}
	  it("the convert action should load the appropriate converter") {
		  val converters = new java.util.HashSet[IConverter]()
		  val converter1 = mock[IConverter]
		  converter1 stubs 'formats returning List[String]("pdf")
		  converter1 stubs 'hashCode returning 1
		  val converter2 = mock[IConverter]
		  converter2 stubs 'formats returning List[String]("png")
		  converter2 stubs 'hashCode returning 2
		  
		  converters.add(converter1)
		  converters.add(converter2)
		  
		  val file = new File("test.pdf")
		  val importFile = new ArchiveFile(_originalFile = file)
		  val file2 = new File("test.png")
		  val importFile2 = new ArchiveFile(_originalFile = file2)
		  val file3 = new File("test.impossible")
		  val importFile3 = new ArchiveFile(_originalFile = file3)
		  
		  inSequence {
			  converter1 expects 'convert withArgs(file) returning file
			  converter2 expects 'convert withArgs(file2) returning file2
		  }
		  
		  val convertAction = new ConvertAction(converters)
		  convertAction.execute(importFile) should be (true)
		  convertAction.execute(importFile2) should be (true)
		  convertAction.execute(importFile3) should be (false)
	  }
	  it("the ocr action should load the best ocr engine") {
		  val ocrEngines = new java.util.HashSet[IOCR]()
		  val ocr1 = mock[IOCR]
		  ocr1 stubs 'formats returning List[String]("txt")
		  ocr1 stubs 'hashCode returning 1
		  val ocr2 = mock[IOCR]
		  ocr2 stubs 'formats returning List[String]("standard")
		  ocr2 stubs 'hashCode returning 2
		  ocrEngines.add(ocr1)
		  ocrEngines.add(ocr2)
		  
		  val ocrAction = new OCRAction(ocrEngines)
		  
		  val file = new File("test.txt")
		  val importFile = new ArchiveFile(_originalFile = file)
		  val file2 = new File("test.png")
		  val importFile2 = new ArchiveFile(_originalFile = file2)
		  
		  inSequence {
			  ocr1 expects 'doOCR withArgs(importFile) returning "1"
			  ocr2 expects 'doOCR withArgs(importFile2) returning "2"
		  }
		  ocrAction.execute(importFile) should be (true)
		  ocrAction.execute(importFile2) should be (true)
		  importFile.fullText should be ("1")
		  importFile2.fullText should be ("2")
	  }
	  it("the save action should save the file into the database") {
		  val importFileRepository = mock[IArchiveFileRepository]
		  val importFile = new ArchiveFile(_originalFile = new File("."))
		  val saveAction = new SaveAction(importFileRepository)
		  
		  inSequence {
			  importFileRepository expects 'addFile withArgs(importFile) returning true
		  }
		  saveAction.execute(importFile) should be (true)
	  }
	  it("the sorting action should sort the file into the correct archive") {
		  val sortTester = new SortTester()
		  val settings = mock[ISettings]
		  val archive = mock[IArchiveRepository]
		  val sortingAction = new SortingAction(sortTester, settings, archive)
		  val importFile = new ArchiveFile(_originalFile = new File("."))
		  Sort.sortTester = sortTester
		  val sortDefinition = Sort to 'testArchive
		  
		  val retArchive = new Archive(identity = "testArchive", name = "testArchive")
		  
		  inSequence {
			  archive expects 'getArchive withArgs("testArchive") returning Option(retArchive)
		  }
		  
		  val ret = sortingAction.execute(importFile)
		  ret should be(true)
		  importFile.archiveIdentity should be(retArchive.identity)
	  }
	  it("the sorting action should sort to the default archive if none is found") {
		  val sortTester = mock[ISortTester]
		  val settings = mock[ISettings]
		  val archive = mock[IArchiveRepository]
		  val sortingAction = new SortingAction(sortTester, settings, archive)
		  val importFile = new ArchiveFile(_originalFile = new File("."))
		  val sortDefinition = Sort to 'testArchive
		  
		  val retArchive = new Archive(identity = "default", name = "default")
		  
		  inSequence {
			  sortTester expects 'testFile withArgs(importFile) returning None
			  settings expects 'defaultArchive returning 'default
			  archive expects 'getArchive withArgs("default") returning Option(retArchive)
		  }
		  
		  val ret = sortingAction.execute(importFile)
		  ret should be(true)
		  importFile.archiveIdentity should be (retArchive.identity)
	  }
	  it("the label action should label the archive correctly") {
		  val labelTester = mock[ILabelTester]
		  val labelDef = mock[ILabelDefinition]
		  labelDef stubs 'toString withArgs("") returning "labelDef"
		  labelDef stubs 'toString returning "labelDef"
		  
		  val labelAction = new LabelAction(labelTester)
		  
		  inSequence {
			  labelTester expects 'testFile returning Some(labelDef)
			  labelDef expects 'labelFile
		  }
		  
		  val ret = labelAction.execute(new ArchiveFile())
		  
		  ret should be(true)
	  }
	  it("the sorting archive should return false if the archive isn't found") {
		  val sortTester = mock[ISortTester]
		  val settings = mock[ISettings]
		  val archive = mock[IArchiveRepository]
		  val sortingAction = new SortingAction(sortTester, settings, archive)
		  val importFile = new ArchiveFile(_originalFile = new File("."))
		  val sortDefinition = Sort to 'testArchive
		  
		  inSequence {
			  sortTester expects 'testFile withArgs(importFile) returning Option(sortDefinition)
			  archive expects 'getArchive withArgs("testArchive") returning None
		  }
		  
		  val ret = sortingAction.execute(importFile)
		  ret should be(false)
	  }
	  it("the workflow action should add the workflows to the file") {
		  val workflowTester = mock[IWorkflowTester]
		  val workflowAction = new WorkflowAction(workflowTester)
		  val importFile = new ArchiveFile(_originalFile = new File("."))
		  
		  val workflow1 = new Workflow(identity = "workflow1", groups = ListBuffer[String](), users = ListBuffer[String]())
		  val workflow2 = new Workflow(identity = "workflow1", groups = ListBuffer[String](), users = ListBuffer[String]())
		  val workflows = ListBuffer[Workflow](workflow1, workflow2)
		  
		  inSequence {
			  workflowTester expects 'getWorkflows withArgs(importFile, true) returning workflows
		  }
		  
		  val ret = workflowAction execute importFile
		  ret should be(true)
		  workflow1.archiveFileId should be(importFile.id)
		  workflow2.archiveFileId should be(importFile.id)
		  importFile.workflows should have length(2)
		  importFile.workflows should contain(workflow1)
		  importFile.workflows should contain(workflow2)
	  }
	}
}