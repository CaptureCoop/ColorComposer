package org.capturecoop.cccolorutils

import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

fun Color.setRed(value: Int) = CCColorUtils.setColorRed(this, value)
fun Color.setGreen(value: Int) = CCColorUtils.setColorGreen(this, value)
fun Color.setBlue(value: Int) = CCColorUtils.setColorBlue(this, value)
fun Color.setAlpha(value: Int) = CCColorUtils.setColorAlpha(this, value)
fun Color.toHex() = CCColorUtils.rgb2hex(this)

fun Rectangle.draw(g: Graphics) = CCColorUtils.drawRect(g, this)
fun Rectangle.fill(g: Graphics) = CCColorUtils.fillRect(g, this)