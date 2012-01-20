package ch.filefarmer.poso

class ArchiveTree(val archives: collection.mutable.Map[Archive, Option[ArchiveTree]] = collection.mutable.Map[Archive, Option[ArchiveTree]]()) {
	def count() = {
		archives.count(p => true)
	}
	
	def apply(identity:String) = {
		archives.find(p => p._1.identity == identity)
	}
}