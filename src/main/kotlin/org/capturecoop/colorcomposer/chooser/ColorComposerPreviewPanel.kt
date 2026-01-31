package org.capturecoop.colorcomposer.chooser

import java.awt.Dimension
import java.awt.GridLayout
import java.awt.image.BufferedImage
import javax.swing.JPanel
import javax.swing.JTabbedPane

class ColorComposerPreviewPanel(colorComposer: ColorComposer, useGradient: Boolean, previewBackground: BufferedImage?) : JPanel() {
    private var panelSingle = ColorComposerSinglePreview(colorComposer, previewBackground)
    private var panelGradient: ColorComposerGradientPreview? = if (useGradient) ColorComposerGradientPreview(colorComposer, previewBackground) else null
    private var tabPane = JTabbedPane(JTabbedPane.TOP)

    init {
        preferredSize = Dimension(0, 256)
        layout = GridLayout()
        colorComposer.setterPanel.addChangeListener {
            colorComposer.setterPanel.color.also { spc ->
                when(tabPane.selectedIndex) {
                    0 -> colorComposer.color.primaryColor = spc
                    1 ->panelGradient!!.setColorAuto(spc)
                }
            }
        }
        tabPane.addTab("Single color", panelSingle)
        if (useGradient) tabPane.addTab("Gradient", panelGradient)
        if (colorComposer.color.isGradient && useGradient) tabPane.selectedIndex = 1
        tabPane.addChangeListener {
            when (tabPane.selectedIndex) {
                0 -> colorComposer.color.isGradient = false
                1 -> colorComposer.color.isGradient = true
            }
        }
        add(tabPane)
    }
}