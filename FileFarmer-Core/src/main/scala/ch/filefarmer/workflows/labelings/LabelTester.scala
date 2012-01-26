package ch.filefarmer.workflows.labelings
import ch.filefarmer.poso.ArchiveFile

class LabelTester extends ILabelTester {
	def testFile(file:ArchiveFile):Option[ILabelDefinition] = {
		definitions.find(d => {
			d.archiveName.toLowerCase == file.archiveIdentity.toLowerCase && d.checkFile(file)
		})
	}
}