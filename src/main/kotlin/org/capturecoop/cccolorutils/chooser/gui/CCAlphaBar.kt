package org.capturecoop.cccolorutils.chooser.gui

import org.capturecoop.cccolorutils.CCColorUtils.DIRECTION
import org.capturecoop.cccolorutils.CCColorUtils.createAlphaBar
import org.capturecoop.cccolorutils.fill
import org.capturecoop.ccutils.math.CCVector2Float
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.JPanel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class CCAlphaBar(private var backgroundColor: Color, private val direction: DIRECTION, alwaysGrab: Boolean) : JPanel() {
    private var alpha: Float
    private var isDragging = false
    private var buffer: BufferedImage? = null
    private var dirty = true
    private val changeListeners = ArrayList<ChangeListener>()

    init {
        alpha = backgroundColor.alpha / 255f
        gridImage = ImageIO.read(CCAlphaBar::class.java.getResource("/org/capturecoop/cccolorutils/transparent_grid_4x4.png"))

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
        if (direction === DIRECTION.HORIZONTAL) {
            pos = x
            size = width
        }
        val percentage = pos * 100f / size
        alpha = CCVector2Float(percentage / 100f, 0.0f).limitX(0f, 1f).x
        repaint()
        changeListeners.forEach { it.stateChanged(ChangeEvent(this)) }
    }

    private val sizeX: Int
        get() = width - MARGIN
    private val sizeY: Int
        get() = height - MARGIN
    private val selectRect: Rectangle
        get() = when (direction) {
            DIRECTION.VERTICAL -> {
                val yPos = (sizeY / (1 / alpha)).toInt() + MARGIN / 2
                Rectangle(SEL_MARGIN_OFF, yPos - SEL_MARGIN, width - SEL_MARGIN_OFF * 2, SEL_MARGIN * 2)
            }
            DIRECTION.HORIZONTAL -> {
                val xPos = (sizeX / (1 / alpha)).toInt() + MARGIN / 2
                Rectangle(xPos - SEL_MARGIN, SEL_MARGIN_OFF, SEL_MARGIN * 2, height - SEL_MARGIN_OFF * 2)
            }
        }


    override fun paint(g: Graphics) {
        if (buffer == null || !(buffer!!.width == width && buffer!!.height == height)) {
            buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        }
        val sizeX = sizeX
        val sizeY = sizeY
        g.color = background
        g.fillRect(0, 0, width, height)
        if (dirty) {
            val bufferGraphics = buffer!!.graphics
            if (direction === DIRECTION.HORIZONTAL) {
                for (i in 0 until sizeX / sizeY)
                    bufferGraphics.drawImage(gridImage, MARGIN / 2 + i * sizeY, MARGIN / 2, sizeY, sizeY, this)
            } else {
                for (i in 0 until sizeY / sizeX)
                    bufferGraphics.drawImage(gridImage, MARGIN / 2, MARGIN / 2 + i * sizeX, sizeX, sizeX, this)
            }
            bufferGraphics.drawImage(createAlphaBar(backgroundColor, sizeX, sizeY, direction), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this)
            bufferGraphics.dispose()
            dirty = false
        }
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

    fun setBackgroundColor(color: Color) {
        backgroundColor = color
        dirty = true
        repaint()
    }

    fun setAlpha(alpha: Int) {
        this.alpha = alpha / 255f
        dirty = true
        repaint()
    }

    fun getAlpha(): Int {
        return (alpha * 255f).toInt()
    }

    companion object {
        private const val MARGIN = 10
        private const val SEL_MARGIN = 4
        private const val SEL_MARGIN_OFF = 2
        private lateinit var gridImage: BufferedImage
    }
}