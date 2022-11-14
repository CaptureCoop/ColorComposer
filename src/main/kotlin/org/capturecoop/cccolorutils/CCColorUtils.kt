package org.capturecoop.cccolorutils

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.image.BufferedImage

object CCColorUtils {
    fun rgb2hex(color: Color) = String.format("#%02x%02x%02x", color.red, color.green, color.blue)

    fun hex2rgb(colorStr: String) = try {
        val r = Integer.valueOf(colorStr.substring(1, 3), 16)
        val g = Integer.valueOf(colorStr.substring(3, 5), 16)
        val b = Integer.valueOf(colorStr.substring(5, 7), 16)
        Color(r, g, b)
    } catch (exception: Exception) {
        null
    }

    fun createAlphaBar(color: Color, width: Int, height: Int, direction: DIRECTION): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = image.graphics as Graphics2D
        var amount = width
        var step = 255f / width
        var alpha = 0f
        if (direction == DIRECTION.VERTICAL) {
            amount = height
            step = 255f / height
        }
        for (pos in 0 until amount) {
            g.color = Color(color.red, color.green, color.blue, alpha.toInt())
            when (direction) {
                DIRECTION.VERTICAL -> g.drawLine(0, pos, width, pos)
                DIRECTION.HORIZONTAL -> g.drawLine(pos, 0, pos, height)
            }
            alpha += step
        }
        g.dispose()
        return image
    }

    fun createHSVHueBar(width: Int, height: Int, direction: DIRECTION): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = image.graphics as Graphics2D
        var hue = 0f
        var amount = width
        var step = 1f / width
        if (direction == DIRECTION.VERTICAL) {
            amount = height
            step = 1f / height
        }
        for (pos in 0 until amount) {
            g.color = CCHSB(hue, 1f, 1f).asColor()
            when (direction) {
                DIRECTION.VERTICAL -> g.drawLine(0, pos, width, pos)
                DIRECTION.HORIZONTAL -> g.drawLine(pos, 0, pos, height)
            }
            hue += step
        }
        g.dispose()
        return image
    }

    fun createHSVBox(width: Int, height: Int, hue: Float): BufferedImage {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = image.graphics as Graphics2D
        var brightness = 1f
        var saturation: Float
        val stepHeight = 1f / height
        val stepWidth = 1f / width
        for (y in 0 until height) {
            saturation = 0f
            for (x in 0 until width) {
                saturation += stepWidth
                g.color = CCHSB(hue, saturation, brightness).asColor()
                g.drawLine(x, y, x, y)
            }
            brightness -= stepHeight
        }
        g.dispose()
        return image
    }

    fun toStringColor(c: Color?) = if (c == null) null else "Color[r: ${c.red}, g: ${c.green}, b: ${c.blue}, a: ${c.alpha}]"

    fun setColorRed(c: Color, red: Int) = Color(red, c.green, c.blue, c.alpha)
    fun setColorGreen(c: Color, green: Int) = Color(c.red, green, c.blue, c.alpha)
    fun setColorBlue(c: Color, blue: Int) = Color(c.red, c.green, blue, c.alpha)
    fun setColorAlpha(c: Color, alpha: Int) = Color(c.red, c.green, c.blue, alpha)

    fun drawRect(g: Graphics, rect: Rectangle) = g.drawRect(rect.x, rect.y, rect.width, rect.height)
    fun fillRect(g: Graphics, rect: Rectangle) = g.fillRect(rect.x, rect.y, rect.width, rect.height)

    fun getContrastColor(color: Color): Color {
        val y = (299f * color.red + 587 * color.green + 114 * color.blue / 1000).toDouble()
        return if (y >= 128) Color.black else Color.white
    }

    enum class DIRECTION { VERTICAL, HORIZONTAL }
}