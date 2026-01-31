package org.capturecoop.colorcomposer.chooser

import org.capturecoop.colorcomposer.ColorUtils
import org.capturecoop.colorcomposer.HSB
import org.capturecoop.colorcomposer.fill
import org.capturecoop.defaultdepot.math.Vector2F
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class HSBHueBar(color: Color?, direction: BarDirection, alwaysGrab: Boolean) : JPanel() {
    var hue: Float = 0.0f
        set(value) {
            field = value
            repaint()
        }
    private val direction: BarDirection
    private var isDragging = false
    private var buffer: BufferedImage? = null
    private val changeListeners = ArrayList<ChangeListener>()

    init {
        hue = HSB(color!!).hue
        this.direction = direction
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(mouseEvent: MouseEvent) {
                if (selectRect.contains(mouseEvent.point) && !alwaysGrab) isDragging = true
            }

            override fun mouseReleased(mouseEvent: MouseEvent) {
                isDragging = false
                if (alwaysGrab) execute(mouseEvent.x, mouseEvent.y)
            }
        })
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(mouseEvent: MouseEvent) {
                if (isDragging || alwaysGrab) execute(mouseEvent.x, mouseEvent.y)
            }
        })
    }

    private fun execute(x: Int, y: Int) {
        var pos = y
        var size = height
        if (direction === BarDirection.HORIZONTAL) {
            pos = x
            size = width
        }
        val percentage = pos * 100f / size
        hue = Vector2F(percentage / 100f, 0.0f).limitX(0.0001f, 0.9999f).x
        repaint()
        for (listener in changeListeners) listener.stateChanged(ChangeEvent(this))
    }

    private val sizeX: Int
        get() = width - MARGIN
    private val sizeY: Int
        get() = height - MARGIN
    private val selectRect: Rectangle
        get() = when (direction) {
            BarDirection.VERTICAL -> {
                val yPos = (sizeY / (1 / hue)).toInt() + MARGIN / 2
                Rectangle(SEL_MARGIN_OFF, yPos - SEL_MARGIN, width - SEL_MARGIN_OFF * 2, SEL_MARGIN * 2)
            }
            BarDirection.HORIZONTAL -> {
                val xPos = (sizeX / (1 / hue)).toInt() + MARGIN / 2
                Rectangle(xPos - SEL_MARGIN, SEL_MARGIN_OFF, SEL_MARGIN * 2, height - SEL_MARGIN_OFF * 2)
            }
        }

    override fun paint(g: Graphics) {
        val sizeX = sizeX
        val sizeY = sizeY
        if (buffer == null || !(buffer!!.width == width && buffer!!.height == height)) {
            buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val bufferGraphics = buffer!!.graphics
            bufferGraphics.drawImage(ColorUtils.createHSVHueBar(sizeX, sizeY, direction), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this)
            bufferGraphics.dispose()
        }
        g.color = background
        g.fillRect(0, 0, width, height)
        g.drawImage(buffer, 0, 0, this)
        g.color = Color.BLACK
        g.drawRect(MARGIN / 2 - 1, MARGIN / 2 - 1, sizeX + 1, sizeY + 1)
        g.color = Color.GRAY
        selectRect.fill(g)
    }

    fun addChangeListener(listener: ChangeListener) {
        //These only fire on changes made by the user
        changeListeners.add(listener)
    }

    companion object {
        private const val MARGIN = 10
        private const val SEL_MARGIN = 4
        private const val SEL_MARGIN_OFF = 2
    }
}