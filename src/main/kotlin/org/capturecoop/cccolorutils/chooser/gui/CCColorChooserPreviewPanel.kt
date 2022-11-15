package org.capturecoop.cccolorutils.chooser.gui

import org.capturecoop.cccolorutils.chooser.CCColorChooser
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.JTabbedPane

class CCColorChooserPreviewPanel(colorChooser: CCColorChooser, useGradient: Boolean, previewBackground: BufferedImage?) : JPanel() {
    private var panelSingle = CCColorChooserSinglePreview(colorChooser, previewBackground)
    private var panelGradient: CCColorChooserGradientPreview? = if (useGradient) CCColorChooserGradientPreview(colorChooser, previewBackground) else null
    private var tabPane = JTabbedPane(JTabbedPane.TOP)

    init {
        preferredSize = Dimension(0, 256)
        layout = GridLayout()
        colorChooser.setterPanel.addChangeListener {
            colorChooser.setterPanel.color.also { spc ->
                when(tabPane.selectedIndex) {
                    0 -> colorChooser.color.primaryColor = spc
                    1 ->panelGradient!!.setColorAuto(spc)
                }
            }
        }
        tabPane.addTab("Single color", panelSingle)
        if (useGradient) tabPane.addTab("Gradient", panelGradient)
        if (colorChooser.color.isGradient && useGradient) tabPane.selectedIndex = 1
        tabPane.addChangeListener {
            when (tabPane.selectedIndex) {
                0 -> colorChooser.color.isGradient = false
                1 -> colorChooser.color.isGradient = true
            }
        }
        add(tabPane)
    }
}