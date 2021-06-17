package ui

import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object ResourceImage {
    val icon = loadImageResource("pdf.png")
}

fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    return resource.openStream().use(ImageIO::read)
}