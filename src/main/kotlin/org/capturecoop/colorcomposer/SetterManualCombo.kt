package org.capturecoop.colorcomposer

import javax.swing.JSlider
import javax.swing.JSpinner

class SetterManualCombo(val slider: JSlider? = null, val spinner: JSpinner? = null) {
    //Remove once port is complete
    constructor(slider: JSlider): this(slider, null)
    constructor(spinner: JSpinner): this(null, spinner)

    fun setValue(value: Int) {
        slider?.value = value
        spinner?.value = value
    }

    fun getValue(): Int {
        if(slider != null) return slider.value
        if(spinner != null) return spinner.value as Int
        return -1;
    }
}