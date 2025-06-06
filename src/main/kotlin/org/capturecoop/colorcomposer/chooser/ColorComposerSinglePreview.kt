package org.capturecoop.colorcomposer.chooser

import org.capturecoop.colorcomposer.ComposedColor
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class ColorComposerSinglePreview(colorComposer: ColorComposer, private val previewBackground: BufferedImage?) : JPanel() {
    private val color: ComposedColor = colorComposer.color

    init {
        color.addChangeListener{ repaint() }
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        val p = width / 2 - height / 2
        previewBackground?.let { g.drawImage(it.getSubimage(0, 0, height, height), p, 0, height, height, null) }
        g.color = color.primaryColor
        g.fillRect(p, 0, height, height)
    }
}