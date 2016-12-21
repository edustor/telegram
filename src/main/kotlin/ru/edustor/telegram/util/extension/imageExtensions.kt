package ru.edustor.telegram.util.extension

import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun Image.getAsByteArray(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    ImageIO.setUseCache(false)
    ImageIO.write(this as BufferedImage, "png", outputStream)
    return outputStream.toByteArray()
}