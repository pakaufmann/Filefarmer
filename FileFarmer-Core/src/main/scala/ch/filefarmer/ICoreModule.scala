package ch.filefarmer
import ch.filefarmer.importer.actions.IAction

/**
 * trait for the core module
 */
trait ICoreModule {
	def addActionAfter(actionClass: Class[_ <: IAction], after: Class[_ <: IAction])
	def addActionBefore(actionClass: Class[_ <: IAction], before: Class[_ <: IAction])
}