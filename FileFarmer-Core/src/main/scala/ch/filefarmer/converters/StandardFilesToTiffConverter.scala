package ch.filefarmer.converters

import java.io.File
import java.awt.image.BufferedImage
import java.awt.Rectangle
import java.awt.Dimension
import com.google.inject.Inject
import ch.filefarmer.settings.ISettings
import org.im4java.core.IMOperation
import org.im4java.core.ConvertCmd

/**
 * converts some standard formats to a tiff file
 * following formats are covered by this converter:
 * txt, png, jpg, jpeg, gif, bmp, pdf, ps
 */
class StandardFilesToTiffConverter@Inject()(val settings: ISettings) extends IConverter {
	/**
	 * @see IConverter
	 */
	def formats() = List[String]("txt", "png", "jpg", "jpeg", "gif", "bmp", "pdf", "ps")
	
	/**
	 * @see IConverter
	 */
	def convert(importFile: File) = {
	  val convertCmd = new ConvertCmd()
	  val convertedFile = new File(settings.tempFolder + "/" + importFile.getName() + ".tif")
	  
	  val operation = new IMOperation()
	  operation.addImage()
	  operation.depth(8)
	  operation.alpha("off")
	  operation.addImage()
	  
	  convertCmd.run(operation, importFile.getAbsolutePath(), convertedFile.getCanonicalPath())
	  convertedFile
	  }
}