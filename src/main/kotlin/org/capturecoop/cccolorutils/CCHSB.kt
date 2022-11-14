package org.capturecoop.cccolorutils

import java.awt.Color

class CCHSB {
    val hue: Float
    val saturation: Float
    val brightness: Float
    val alpha: Int

    constructor(hue: Float, saturation: Float, brightness: Float, alpha: Int = 255) {
        this.hue = hue
        this.saturation = saturation
        this.brightness = brightness
        this.alpha = alpha
    }

    constructor(color: Color) {
        val values = floatArrayOf(0.0F, 0.0F, 0.0F)
        Color.RGBtoHSB(color.red, color.green, color.blue, values)
        this.hue = values[0]
        this.saturation = values[1]
        this.brightness = values[2]
        this.alpha = color.alpha
    }

    fun asColor(): Color {
        Color(Color.HSBtoRGB(hue, saturation, brightness)).also {
            return Color(it.red, it.green, it.blue, alpha)
        }
    }

    override fun toString() = "CCHSB[hue: $hue, saturation: $saturation, brightness: $brightness, alpha: $alpha]"
    override fun equals(other: Any?): Boolean {
        if(other == null || other != this) return false
        if(other !is CCHSB) return false
        return hue == other.hue && saturation == other.saturation && brightness == other.brightness && alpha == other.alpha
    }

    override fun hashCode(): Int {
        var result = hue.hashCode()
        result = 31 * result + saturation.hashCode()
        result = 31 * result + brightness.hashCode()
        result = 31 * result + alpha
        return result
    }

}