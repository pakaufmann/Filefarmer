package ch.filefarmer.importer.actions
import ch.filefarmer.poso.ArchiveFile
import java.io.File

/**
 * deletes all files from the filesystem
 */
class CleanupAction extends IAction {
  
  /**
   * @see IAction
   */
  def execute(file: ArchiveFile) = {
    file.originalFile.delete()
    file.tiffFile.delete()
    val txtFile = new File(file.tiffFile.getAbsolutePath() + ".txt")
    txtFile.delete()
    true
  }
}