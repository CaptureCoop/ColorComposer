package org.capturecoop.cccolorutils.chooser

import org.capturecoop.cccolorutils.CCColor
import org.capturecoop.cccolorutils.chooser.gui.CCColorChooserPreviewPanel
import org.capturecoop.cccolorutils.chooser.gui.CCColorChooserSetterPanel
import org.capturecoop.ccutils.utils.CCIClosable
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

class CCColorChooser(color: CCColor = CCColor(Color.WHITE), title: String = "Color Chooser", parent: JFrame? = null, x: Int = -Integer.MAX_VALUE, y: Int = -Integer.MAX_VALUE, useGradient: Boolean = color.isGradient, backgroundImage: BufferedImage? = null, icon: BufferedImage? = null): JFrame(), CCIClosable {
    var color: CCColor = color
        set(value) {
            color.loadFromCCColor(value)
            //Update setter, preview panel updates automatically via a listener in cccolor
            setterPanel.setColor(color.primaryColor, alertListeners = true, updateComponents = true)
        }
    private val previewPanel: CCColorChooserPreviewPanel
    val setterPanel: CCColorChooserSetterPanel
    private val changeListeners = ArrayList<ChangeListener>()
    private val colorChangeListener: ChangeListener
    private var onClose: ((CCColorChooser) -> (Unit))? = null

    init {
        setterPanel = CCColorChooserSetterPanel(color.primaryColor, this)
        previewPanel = CCColorChooserPreviewPanel(this, useGradient, backgroundImage)
        colorChangeListener = ChangeListener { alertChangeListeners() }
        color.addChangeListener(colorChangeListener)
        setTitle(title)
        if (icon != null) iconImage = icon
        init(x, y, parent)
    }

    private fun init(x: Int, y: Int, parent: JFrame?) {
        val mainPanel = JPanel()
        val submitButtonPanel = JPanel()
        val submit = JButton("Okay")
        submit.addActionListener { close() }
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.add(previewPanel)
        mainPanel.add(setterPanel)
        mainPanel.add(submitButtonPanel)
        add(mainPanel)
        isFocusable = true
        isAlwaysOnTop = true
        pack()
        submit.preferredSize = Dimension(width / 2, 50)
        submitButtonPanel.add(submit)
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

    fun setOnClose(action: (CCColorChooser) -> (Unit)) {
        onClose = action
    }

    override fun close() {
        onClose?.invoke(this)
        color.removeChangeListener(colorChangeListener)
        dispose()
    }
}