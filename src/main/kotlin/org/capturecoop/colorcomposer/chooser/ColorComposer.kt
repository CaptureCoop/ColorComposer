package org.capturecoop.colorcomposer.chooser


import org.capturecoop.colorcomposer.ComposedColor
import org.capturecoop.defaultdepot.Closable
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class ColorComposer(
    color: ComposedColor = ComposedColor(Color.WHITE),
    title: String = "Color Composer",
    parent: JFrame? = null,
    x: Int = -Integer.MAX_VALUE,
    y: Int = -Integer.MAX_VALUE,
    useGradient: Boolean = color.isGradient,
    backgroundImage: BufferedImage? = null,
    icon: BufferedImage? = null
): JFrame(), Closable {
    var color: ComposedColor = color
        set(value) {
            field = value
            //Update setter, preview panel updates automatically via a listener in ComposedColor
            setterPanel.setColor(color.primaryColor, alertListeners = true, updateComponents = true)
        }
    private val previewPanel: ColorComposerPreviewPanel
    val setterPanel: ColorChooserSetterPanel
    private val changeListeners = ArrayList<ChangeListener>()
    private val colorChangeListener: ChangeListener
    private var onClose: ((ColorComposer) -> (Unit))? = null

    init {
        setterPanel = ColorChooserSetterPanel(color.primaryColor, this)
        previewPanel = ColorComposerPreviewPanel(this, useGradient, backgroundImage)
        colorChangeListener = ChangeListener { alertChangeListeners() }
        color.addChangeListener(colorChangeListener)
        setTitle(title)
        if (icon != null) iconImage = icon
        init(x, y, parent)
    }

    private fun init(x: Int, y: Int, parent: JFrame?) {
        val mainPanel = JPanel()
        val submitButtonPanel = JPanel()
        val colorOptionsPanel = JPanel()
        val submit = JButton("Okay")
        submit.addActionListener { close() }
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.add(previewPanel)
        mainPanel.add(setterPanel)
        mainPanel.add(colorOptionsPanel)
        mainPanel.add(submitButtonPanel)
        add(mainPanel)
        isFocusable = true
        isAlwaysOnTop = true
        pack()
        submit.preferredSize = Dimension(width / 2, 50)
        submitButtonPanel.add(submit)
        colorOptionsPanel.add(ColorOptionsPanel())
        pack()
        if(parent == null) {
            Toolkit.getDefaultToolkit().screenSize.also {
                val posX = if(x != -Integer.MAX_VALUE) x else it.width / 2 - width / 2
                val posY = if(y != -Integer.MAX_VALUE) y else it.height / 2 - height / 2
                location = Point(posX, posY)
            }
        } else {
            location = Point(parent.location.x + parent.width / 2 - width / 2, parent.location.y + parent.height  / 2 - height / 2)
        }
        isVisible = true
        addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                close()
            }
        })
    }

    private fun alertChangeListeners() {
        changeListeners.forEach { it.stateChanged(ChangeEvent(this)) }
    }

    fun setOnClose(action: (ColorComposer) -> (Unit)) {
        onClose = action
    }

    override fun close() {
        onClose?.invoke(this)
        color.removeChangeListener(colorChangeListener)
        dispose()
    }
}