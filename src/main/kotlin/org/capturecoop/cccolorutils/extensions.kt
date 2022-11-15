package org.capturecoop.cccolorutils

import java.awt.Color

fun Color.setRed(value: Int) = CCColorUtils.setColorRed(this, value)
fun Color.setGreen(value: Int) = CCColorUtils.setColorGreen(this, value)
fun Color.setBlue(value: Int) = CCColorUtils.setColorBlue(this, value)
fun Color.setAlpha(value: Int) = CCColorUtils.setColorAlpha(this, value)
fun Color.toHex() = CCColorUtils.rgb2hex(this)