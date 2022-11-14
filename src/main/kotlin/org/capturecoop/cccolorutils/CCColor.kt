package org.capturecoop.cccolorutils

import org.capturecoop.ccutils.math.CCVector2Float
import org.capturecoop.ccutils.math.CCVector2Int
import java.awt.Color
import java.awt.GradientPaint
import java.awt.Paint
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class CCColor {
    var primaryColor: Color
        set(value) {
            field = value
            alertChangeListeners()
        }
    var secondaryColor: Color? = null
        set(value) {
            field = value
            alertChangeListeners()
        }
    var point1: CCVector2Float? = null
        set(value) {
            if(value != null) {
                field = CCVector2Float(value)
                field!!.limit(0.0F, 1.0F)
            } else field = null
            alertChangeListeners()
        }
    var point2: CCVector2Float? = null
        set(value) {
            if(value != null) {
                field = CCVector2Float(value)
                field!!.limit(0.0F, 1.0F)
            } else field = null
            alertChangeListeners()
        }
    var isGradient = false
        set(value) {
            field = value
            ensureSecondaryColor()
            alertChangeListeners()
        }

    val changeListeners = ArrayList<ChangeListener>()
    enum class ColorType { PRIMARY, SECONDARY }

    constructor() {
        primaryColor = Color.WHITE
    }

    constructor(color: Color) {
        primaryColor = color
    }

    constructor(color: CCColor) {
        primaryColor = color.primaryColor
        secondaryColor = color.secondaryColor
        isGradient = color.isGradient
        color.point1?.let { point1 = it }
        color.point2?.let { point2 = it }
    }

    constructor(color: CCColor, alpha: Int) {
        color.primaryColor.let { primaryColor = it.setAlpha(alpha) }
        color.secondaryColor?.let { secondaryColor = it.setAlpha(alpha) }
        isGradient = color.isGradient
        color.point1?.let { point1 = it }
        color.point2?.let { point2 = it }
    }

    constructor(primaryColor: Color, secondaryColor: Color, isGradient: Boolean) {
        this.primaryColor = primaryColor
        this.secondaryColor = secondaryColor
        this.isGradient = isGradient
    }

    constructor(color: Color, alpha: Int) {
        primaryColor = color.setAlpha(alpha)
    }

    constructor(r: Int, g: Int, b: Int, a: Int) {
        primaryColor = Color(r, g, b, a)
    }

    fun getColor(type: ColorType) = when(type) {
        ColorType.PRIMARY -> primaryColor
        ColorType.SECONDARY -> secondaryColor
    }

    fun loadFromCCColor(otherColor: CCColor) {
        primaryColor = otherColor.primaryColor
        secondaryColor = otherColor.secondaryColor
        point1 = otherColor.point1
        point2 = otherColor.point2
        isGradient = otherColor.isGradient
        alertChangeListeners()
    }

    fun ensureSecondaryColor() {
        if(secondaryColor == null) secondaryColor = primaryColor.brighter()
    }

    fun isValidGradient() = secondaryColor != null

    private fun alertChangeListeners() {
        changeListeners.forEach { it.stateChanged(ChangeEvent(this)) }
    }

    fun getGradientPaint(width: Int, height: Int) = getGradientPaint(width, height, 0, 0)

    fun getGradientPaint(width: Int, height: Int, posX: Int, posY: Int): Paint {
        if(!isGradient) return primaryColor
        ensureSecondaryColor()
        if(point1 == null) point1 = POINT_PRIMARY_DEFAULT
        if(point2 == null) point2 = POINT_SECONDARY_DEFAULT

        //Long term we should make more convenient functions in CCUtils..
        val point1int = CCVector2Int((point1!!.x * width).toInt(), (point1!!.y * height).toInt())
        val point2int = CCVector2Int((point2!!.x * width).toInt(), (point2!!.y * height).toInt())
        return GradientPaint((point1int.x + posX).toFloat(), (point1int.y + posY).toFloat(), primaryColor, (point2int.x + posX).toFloat(), (point2int.y + posY).toFloat(), secondaryColor);
    }

    fun toSaveString(): String {
        var string = CCColorUtils.rgb2hex(primaryColor)
        if(primaryColor.alpha != 255)
            string += "_a${primaryColor.alpha}"
        if(point1 != null && point1 != POINT_PRIMARY_DEFAULT)
            string += "_x${point1!!.x}_y${point1!!.y}"
        string += "_G${isGradient}"

        if(secondaryColor != null) {
            string += "___${CCColorUtils.rgb2hex(secondaryColor!!)}"
            if(secondaryColor!!.alpha != 255)
                string += "_a${secondaryColor!!.alpha}"
            if(point2 != null && point2 != POINT_SECONDARY_DEFAULT)
                string += "_x${point2!!.x}_y${point2!!.y}"
        }
        return string
    }

    override fun toString() = "CCGradientColor[primaryColor: ${CCColorUtils.toStringColor(primaryColor)}, secondaryColor: ${CCColorUtils.toStringColor(secondaryColor)}, point1: $point1, point2: $point2, isGradient: $isGradient]"

    override fun equals(other: Any?): Boolean {
        if(other == null || other != this) return false
        if(other !is CCColor) return false
        return primaryColor.rgb == other.primaryColor.rgb &&
                primaryColor.alpha == other.primaryColor.alpha &&
                secondaryColor?.rgb == other.secondaryColor?.rgb &&
                secondaryColor?.alpha == other.secondaryColor?.alpha &&
                isGradient == other.isGradient &&
                point1 == other.point1 &&
                point2 == other.point2
    }

    companion object {
        val POINT_PRIMARY_DEFAULT = CCVector2Float(0.0, 0.0)
        val POINT_SECONDARY_DEFAULT = CCVector2Float(0.0, 0.0)

        fun fromSaveString(string: String): CCColor {
            val newColor = CCColor()
            string.split("___").forEachIndexed{ i, part ->
                var alpha = -1
                var color: Color? = null
                val defaultPos = if(i != 0) 1.0F else 0.0F
                val pos = CCVector2Float(defaultPos, defaultPos)
                part.split("_").forEach { str ->
                    val subString = str.substring(1)
                    when(str[0]) {
                        '#' -> color = CCColorUtils.hex2rgb(str)
                        'a' -> alpha = subString.toInt()
                        'x' -> pos.x = subString.toFloat()
                        'y' -> pos.y = subString.toFloat()
                        'G' -> newColor.isGradient = subString.toBoolean()
                    }

                    if(alpha == -1 && color != null)
                        alpha = color!!.alpha

                    if(color != null) {
                        when(i) {
                            0 -> newColor.primaryColor = color!!.setAlpha(alpha)
                            1 -> newColor.secondaryColor = color!!.setAlpha(alpha)
                        }
                    }

                    when(i) {
                        0 -> newColor.point1 = pos
                        1 -> newColor.point2 = pos
                    }
                }


            }
            return newColor
        }
    }
}