import org.apache.pdfbox.Loader
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.tools.imageio.ImageIOUtil
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.max
import kotlin.math.min

object PDFTransformer {

    /**
     * Convert pdf to pngs per page
     */
    fun pdfToPng(
        pdfFile: File
    ) {
        require(pdfFile.isFile) {
            "${pdfFile.absolutePath} is not an valid file!"
        }
        val pdfDocument = Loader.loadPDF(pdfFile)
        val pdfRenderer = PDFRenderer(pdfDocument)
        for (i in 0 until pdfDocument.numberOfPages) {
            val bufferedImage = pdfRenderer.renderImageWithDPI(i, 300f, ImageType.RGB)
            ImageIOUtil.writeImage(bufferedImage, "${pdfFile.nameWithoutExtension}-${i + 1}.png", 300)
        }
        pdfDocument.close()
    }

    /**
     * Convert pdf to a single vertical-aligned png
     */
    fun pdfToSinglePng(
        pdfFile: File,
        startPageNumber: Int = 1,
        endPageNumber: Int = Int.MAX_VALUE,
        outputFileName: String? = null
    ) {
        require(pdfFile.isFile) {
            "${pdfFile.absolutePath} is not an valid file!"
        }
        require(startPageNumber >= 1 && endPageNumber >= startPageNumber - 1) {
            "starPageNumber must >=1 and endPageNumber must >= startPageNumber!"
        }
        val pdfDocument = Loader.loadPDF(pdfFile)
        val pdfRenderer = PDFRenderer(pdfDocument)
        var finalImageBuffer: BufferedImage? = null
        var currentY = 0
        var totalHeight = 0
        var maxWidth = 0
        val data = mutableListOf<Array<Any>>()
        for (i in (startPageNumber - 1) until min(endPageNumber, pdfDocument.numberOfPages)) {
            val bim = pdfRenderer.renderImageWithDPI(i, 300f, ImageType.RGB)
            totalHeight += bim.height
            maxWidth = max(maxWidth, bim.width)
            data.add(arrayOf(bim.width, bim.height, bim.getRGB(0, 0, bim.width, bim.height, null, 0, bim.width)))
        }
        for (i in data.indices) {
            if (i == 0) {
                finalImageBuffer = BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB)
            }
            finalImageBuffer?.setRGB(
                0,
                currentY,
                data[i][0] as Int,
                data[i][1] as Int,
                data[i][2] as IntArray,
                0,
                data[i][0] as Int
            )
            currentY += data[i][1] as Int
        }
        ImageIOUtil.writeImage(finalImageBuffer, "${outputFileName ?: pdfFile.nameWithoutExtension}.png", 300)
        pdfDocument.close()
    }

}