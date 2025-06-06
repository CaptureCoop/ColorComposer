package org.capturecoop.colorcomposer

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

fun Color.setRed(value: Int) = ColorUtils.setColorRed(this, value)
fun Color.setGreen(value: Int) = ColorUtils.setColorGreen(this, value)
fun Color.setBlue(value: Int) = ColorUtils.setColorBlue(this, value)
fun Color.setAlpha(value: Int) = ColorUtils.setColorAlpha(this, value)
fun Color.toHex() = ColorUtils.rgb2hex(this)

fun Rectangle.draw(g: Graphics) = ColorUtils.drawRect(g, this)
fun Rectangle.fill(g: Graphics) = ColorUtils.fillRect(g, this)