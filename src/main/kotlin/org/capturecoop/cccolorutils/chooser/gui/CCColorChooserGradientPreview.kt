package org.capturecoop.cccolorutils.chooser.gui

import org.capturecoop.cccolorutils.CCColor
import org.capturecoop.cccolorutils.CCColorUtils
import org.capturecoop.cccolorutils.chooser.CCColorChooser
import org.capturecoop.cccolorutils.draw
import org.capturecoop.cccolorutils.fill
import org.capturecoop.ccutils.math.CCVector2Float
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.event.ChangeListener

class CCColorChooserGradientPreview(colorChooser: CCColorChooser, private val previewBackground: BufferedImage?) : JPanel() {
    private val color: CCColor = colorChooser.color
    private var lastStartX = 0
    private var lastStartY = 0
    private var lastSize = 0
    private var point1Rect: Rectangle? = null
    private var point2Rect: Rectangle? = null
    private var pointControlled = -1 //-1 -> None, 0 -> Point1, 1 -> Point2
    private var lastPointControlled = 0 //As above, however it is not reset upon mouseReleased but set
    private var previewBuffer: BufferedImage? = null

    init {
        color.addChangeListener { repaint() }
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(mouseEvent: MouseEvent) = mousePressedEvent(mouseEvent)
            override fun mousePressed(mouseEvent: MouseEvent) = mousePressedEvent(mouseEvent)
            override fun mouseReleased(mouseEvent: MouseEvent) = kotlin.run { pointControlled = -1 }

            fun mousePressedEvent(mouseEvent: MouseEvent) {
                if (point1Rect != null && point1Rect!!.contains(mouseEvent.point)) {
                    pointControlled = 0
                    lastPointControlled = 0
                    colorChooser.setterPanel.setColor(color.primaryColor, false, true)
                } else if (point2Rect != null && point2Rect!!.contains(mouseEvent.point)) {
                    pointControlled = 1
                    lastPointControlled = 1
                    colorChooser.setterPanel.setColor(color.secondaryColor, false, true)
                }
                repaint()
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(mouseEvent: MouseEvent) {
                if (pointControlled != -1) {
                    val size = lastSize.toFloat()
                    val x = (mouseEvent.x - lastStartX).toFloat() / size
                    val y = (mouseEvent.y - lastStartY).toFloat() / size
                    when(pointControlled) {
                        0 -> color.point1 = CCVector2Float(x, y)
                        1 -> color.point2 = CCVector2Float(x, y)
                    }
                    repaint()
                }
            }
        })
    }

    fun setColorAuto(newColor: Color) {
        when(lastPointControlled) {
            0 -> color.primaryColor = newColor
            1 -> color.secondaryColor = newColor
        }
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        val p = width / 2 - height / 2
        if (previewBackground != null)
            g.drawImage(previewBackground.getSubimage(0, 0, height, height), p, 0, height, height, null)
        g as Graphics2D
        val offset = 20
        val size = height - offset
        val startX = width / 2 - size / 2
        val startY = offset / 2
        lastStartX = startX
        lastStartY = startY
        lastSize = size
        if (previewBuffer == null || previewBuffer!!.width != size || previewBuffer!!.height != size)
            previewBuffer = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)

        //TODO: Weird bug when resizing window, smaller to larger destroys gradient preview
        //TODO: Found it! Render the preview as its own BufferedImage that we resize whenever you stop resizing the window :) that will fix it!
        previewBuffer!!.graphics.also { previewGraphics ->
            previewGraphics as Graphics2D
            val oldComposite = previewGraphics.composite
            previewGraphics.composite = AlphaComposite.Clear
            previewGraphics.fillRect(0, 0, size, size)
            previewGraphics.composite = oldComposite
            previewGraphics.paint = color.getGradientPaint(size, size)
            previewGraphics.fillRect(0, 0, size, size)
            previewGraphics.dispose()
        }
        g.drawImage(previewBuffer, startX, startY, size, size, this)
        point1Rect = Rectangle(startX - offset / 2 + (lastSize * color.point1!!.x).toInt(), (lastSize * color.point1!!.y).toInt(), offset, offset)
        point2Rect = Rectangle(startX - offset / 2 + (lastSize * color.point2!!.x).toInt(), (lastSize * color.point2!!.y).toInt(), offset, offset)
        g.color = color.primaryColor
        point1Rect!!.fill(g)
        g.color = CCColorUtils.getContrastColor(color.primaryColor)
        point1Rect!!.draw(g)
        if (lastPointControlled == 0) {
            val oldStroke = g.stroke
            g.stroke = BasicStroke(offset / 3f)
            point1Rect!!.draw(g)
            g.stroke = oldStroke
        }
        g.color = color.secondaryColor
        point2Rect!!.fill(g)
        g.color = CCColorUtils.getContrastColor(color.secondaryColor!!)
        point2Rect!!.draw(g)
        if (lastPointControlled == 1) {
            val oldStroke = g.stroke
            g.stroke = BasicStroke(offset / 3f)
            point2Rect!!.draw(g)
            g.stroke = oldStroke
        }
    }
}