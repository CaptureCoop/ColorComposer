package org.capturecoop.cccolorutils.chooser.gui

import org.capturecoop.cccolorutils.CCColor
import org.capturecoop.cccolorutils.chooser.CCColorChooser
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.event.ChangeListener

class CCColorChooserSinglePreview(colorChooser: CCColorChooser, private val previewBackground: BufferedImage?) : JPanel() {
    private val color: CCColor = colorChooser.color

    init {
        color.changeListeners.add(ChangeListener { repaint() })
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        val p = width / 2 - height / 2
        previewBackground?.let { g.drawImage(it.getSubimage(0, 0, height, height), p, 0, height, height, null) }
        g.color = color.primaryColor
        g.fillRect(p, 0, height, height)
    }
}