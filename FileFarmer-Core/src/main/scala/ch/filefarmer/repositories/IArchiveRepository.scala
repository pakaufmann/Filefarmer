package ch.filefarmer.repositories
import ch.filefarmer.poso.Archive

trait IArchiveRepository {
	def getArchive(identity:String): Option[Archive]
	def addArchive(archive:Archive)
}