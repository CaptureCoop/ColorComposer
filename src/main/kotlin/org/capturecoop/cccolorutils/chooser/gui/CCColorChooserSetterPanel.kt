package org.capturecoop.cccolorutils.chooser.gui

import org.capturecoop.cccolorutils.CCColor.Companion.fromSaveString
import org.capturecoop.cccolorutils.CCColorUtils
import org.capturecoop.cccolorutils.CCColorUtils.hex2rgb
import org.capturecoop.cccolorutils.CCColorUtils.rgb2hex
import org.capturecoop.cccolorutils.CCColorUtils.setColorAlpha
import org.capturecoop.cccolorutils.CCColorUtils.setColorBlue
import org.capturecoop.cccolorutils.CCColorUtils.setColorGreen
import org.capturecoop.cccolorutils.CCColorUtils.setColorRed
import org.capturecoop.cccolorutils.CCHSB
import org.capturecoop.cccolorutils.chooser.CCColorChooser
import org.capturecoop.cccolorutils.chooser.CCISetterManualUpdate
import org.capturecoop.cccolorutils.chooser.CCSetterManualCombo
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class CCColorChooserSetterPanel(var color: Color, private val chooser: CCColorChooser) : JPanel() {
    private val changeListeners = ArrayList<ChangeListener>()
    private val sliderUpdateListeners = ArrayList<ChangeListener>()
    private val visualUpdateListeners = ArrayList<ChangeListener>()

    init {
        layout = GridLayout(0, 2)

        //Picker elements
        val picker = CCHSBPicker(color, true)
        val hueBar = CCHSBHueBar(color, CCColorUtils.DIRECTION.VERTICAL, true)
        val alphaBar = CCAlphaBar(color, CCColorUtils.DIRECTION.VERTICAL, true)

        //Set size
        picker.preferredSize = Dimension(256, 256)
        Dimension(32, 256).also {
            hueBar.preferredSize = it
            alphaBar.preferredSize = it
        }
        visualUpdateListeners.add(ChangeListener {
            CCHSB(color).also { hsb ->
                picker.hue = hsb.hue
                picker.saturation = hsb.saturation
                picker.brightness = hsb.brightness
                hueBar.hue = hsb.hue
            }
            alphaBar.setBackgroundColor(color)
            alphaBar.setAlpha(color.alpha)
        })

        //Add visuals
        val visual = JPanel()
        visual.add(picker)
        visual.add(hueBar)
        visual.add(alphaBar)

        //Add Listeners
        picker.addChangeListener {
            setColor(setColorAlpha(picker.color, color.alpha), true, false)
            alphaBar.setBackgroundColor(color)
            updateSliderListeners()
        }
        hueBar.addChangeListener {
            val hue = hueBar.hue
            picker.hue = hue
            setColor(setColorAlpha(picker.color, color.alpha), true, false)
            alphaBar.setBackgroundColor(color)
            updateSliderListeners()
        }
        alphaBar.addChangeListener {
            setColor(setColorAlpha(color, alphaBar.getAlpha()), true, false)
            updateSliderListeners()
        }
        val rgbPanel = JPanel()
        rgbPanel.add(setupRGBSliders(JPanel(GridBagLayout())))
        val hsbPanel = JPanel()
        hsbPanel.add(setupHSBSliders(JPanel(GridBagLayout()), picker, hueBar, alphaBar))
        val options = JTabbedPane()
        options.addTab("RGB", rgbPanel)
        options.addTab("HSB", hsbPanel)
        add(visual)
        add(options)
        updateSliderListeners()
    }

    private fun setupHSBSliders(panel: JPanel, picker: CCHSBPicker, hueBar: CCHSBHueBar, alphaBar: CCAlphaBar): JPanel {
        panel.removeAll()
        val gbc = GridBagConstraints()
        gbc.insets = Insets(5, 5, 5, 5)
        val isSetter = AtomicBoolean(false)
        val hue = createSettings(panel, "Hue", 0, 100, isSetter, gbc) {
            val hueValue = it!!.getValue() / 100f
            picker.hue = hueValue
            hueBar.hue = hueValue
            setColor(setColorAlpha(picker.color, color.alpha), true, false)
            updateVisualListeners()
        }
        val saturationSlider = createSettings(panel, "Saturation", 0, 100, isSetter, gbc) {
                picker.saturation = it!!.getValue() / 100f
                setColor(setColorAlpha(picker.color, color.alpha), true, false)
                updateVisualListeners()
            }
        val brightnessSlider = createSettings(panel, "Brightness", 0, 100, isSetter, gbc) {
                picker.brightness = it!!.getValue() / 100f
                setColor(setColorAlpha(picker.color, color.alpha), true, false)
                updateVisualListeners()
            }
        val alphaSlider = createSettings(panel, "Alpha", 0, 255, isSetter, gbc) {
            alphaBar.setAlpha(it!!.getValue())
            setColor(setColorAlpha(color, it.getValue()), true, false)
            updateVisualListeners()
        }
        createHexInput(panel, gbc)
        createCCColorFormatField(panel, gbc)
        sliderUpdateListeners.add(ChangeListener {
            isSetter.set(true)
            hue.setValue((picker.hue * 100f).toInt())
            saturationSlider.setValue((picker.saturation * 100f).toInt())
            brightnessSlider.setValue((picker.brightness * 100f).toInt())
            alphaSlider.setValue(color.alpha)
            isSetter.set(false)
        })
        return panel
    }

    fun setupRGBSliders(panel: JPanel): JPanel {
        panel.removeAll()
        val gbc = GridBagConstraints()
        gbc.insets = Insets(5, 5, 5, 5)
        val isSetter = AtomicBoolean(false)
        val red = createSettings(panel, "Red", 0, 255, isSetter, gbc) {
            setColor(setColorRed(color, it!!.getValue()), true, false)
            updateVisualListeners()
        }
        val green = createSettings(panel, "Green", 0, 255, isSetter, gbc) {
            setColor(setColorGreen(color, it!!.getValue()), true, false)
            updateVisualListeners()
        }
        val blue = createSettings(panel, "Blue", 0, 255, isSetter, gbc) {
            setColor(setColorBlue(color, it!!.getValue()), true, false)
            updateVisualListeners()
        }
        val alpha = createSettings(panel, "Alpha", 0, 255, isSetter, gbc) {
            setColor(setColorAlpha(color, it!!.getValue()), true, false)
            updateVisualListeners()
        }
        createHexInput(panel, gbc)
        createCCColorFormatField(panel, gbc)
        sliderUpdateListeners.add(ChangeListener {
            isSetter.set(true)
            red.setValue(color.red)
            green.setValue(color.green)
            blue.setValue(color.blue)
            alpha.setValue(color.alpha)
            isSetter.set(false)
        })
        return panel
    }

    private fun createSettings(panel: JPanel, title: String?, min: Int, max: Int, isSetter: AtomicBoolean, gbc: GridBagConstraints, onUpdate: CCISetterManualUpdate): CCSetterManualCombo {
        val slider = JSlider(min, max)
        val spinner = JSpinner()
        slider.minimum = min
        slider.maximum = max
        slider.preferredSize = Dimension(240, 16)
        slider.addChangeListener {
            if (!isSetter.get())
                onUpdate.update(CCSetterManualCombo(slider))
            spinner.value = slider.value
        }
        gbc.gridx = 0
        panel.add(JLabel(title), gbc)
        gbc.gridx = 1
        panel.add(slider, gbc)
        spinner.addChangeListener {
            if (!isSetter.get())
                onUpdate.update(CCSetterManualCombo(spinner))
            slider.value = spinner.value as Int
        }
        spinner.model = SpinnerNumberModel(0, min, max, 1)
        gbc.gridx = 2
        panel.add(spinner, gbc)
        return CCSetterManualCombo(slider, spinner)
    }

    private fun createCCColorFormatField(panel: JPanel, gbc: GridBagConstraints) {
        val textArea = JTextField(chooser.color.toSaveString())
        val size = textArea.preferredSize
        size.width = size.width * 3
        //Avoid getting larger than 300...
        if (size.width > 300) size.width = 300
        textArea.preferredSize = size
        textArea.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE -> {
                        chooser.requestFocus()
                        chooser.color = fromSaveString(textArea.text)
                    }
                }
            }
        })
        chooser.color.addChangeListener { textArea.text = chooser.color.toSaveString() }
        gbc.gridx = 0
        panel.add(JLabel("Save String"), gbc)
        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.WEST
        panel.add(textArea, gbc)
    }

    private fun createHexInput(panel: JPanel, gbc: GridBagConstraints) {
        val textArea = JTextField(rgb2hex(color))
        val size = textArea.preferredSize
        size.width = size.width * 2
        textArea.preferredSize = size
        textArea.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent) {
                when (e.keyCode) {
                    KeyEvent.VK_ENTER, KeyEvent.VK_ESCAPE -> {
                        chooser.requestFocus()
                        val temp = hex2rgb(textArea.text)
                        if (temp != null) setColor(temp, true, true) else textArea.text = rgb2hex(color)
                    }
                }
            }
        })
        val update = ChangeListener { textArea.text = rgb2hex(color) }
        visualUpdateListeners.add(update)
        sliderUpdateListeners.add(update)
        gbc.gridx = 0
        panel.add(JLabel("HEX"), gbc)
        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.WEST
        panel.add(textArea, gbc)
    }

    private fun updateSliderListeners() {
        sliderUpdateListeners.forEach { it.stateChanged(ChangeEvent(this)) }
    }

    fun updateVisualListeners() {
        visualUpdateListeners.forEach { it.stateChanged(ChangeEvent(this)) }
    }

    fun addChangeListener(listener: ChangeListener) {
        changeListeners.add(listener)
    }

    fun setColor(color: Color, alertListeners: Boolean, updateComponents: Boolean) {
        this.color = color
        if (alertListeners) changeListeners.forEach { it.stateChanged(ChangeEvent(this)) }
        if (updateComponents) {
            updateVisualListeners()
            updateSliderListeners()
        }
    }
}