package ch.filefarmer.repositories
import ch.filefarmer.poso._

trait IArchiveRepository {
	def getArchive(identity:String): Option[Archive]
	def addArchive(archive:Archive)
	def getArchiveTree(parent:String = ""): Option[ArchiveTree]
}