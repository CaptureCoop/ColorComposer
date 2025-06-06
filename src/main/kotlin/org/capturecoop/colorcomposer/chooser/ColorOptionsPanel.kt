package org.capturecoop.colorcomposer.chooser

import org.capturecoop.colorcomposer.ComposedColor
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

class ColorOptionsPanel: JPanel() {
    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(JLabel("Recent Colors"))
        val colors = JPanel()
        listOf(
            Color.RED,
            Color.PINK,
            Color.BLUE,
            Color.GREEN,
            Color.MAGENTA,
            Color.LIGHT_GRAY,
            Color.CYAN,
            Color.YELLOW,
            Color.ORANGE
        ).map { ComposedColor(it) }.forEach {
            colors.add(ColorButton(ComposedColor(it)))
        }
        add(colors)
    }
}

class ColorButton(val color: ComposedColor): JPanel() {
    init {
        preferredSize = Dimension(64, 64)
        revalidate()
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        g.color = color.primaryColor
        g.fillRect(0, 0, width, height)
        g.color = Color.BLACK
        g.drawRect(0, 0, width - 1, height - 1)
    }
}