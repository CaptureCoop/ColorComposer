package org.capturecoop.cccolorutils.chooser;

import javax.swing.*;

public class CCSetterManualCombo {
    private final JSlider slider;
    private final JSpinner spinner;

    public CCSetterManualCombo(JSlider slider, JSpinner spinner) {
        this.slider = slider;
        this.spinner = spinner;
    }

    public CCSetterManualCombo(JSlider slider) {
        this(slider, null);
    }

    public CCSetterManualCombo(JSpinner spinner) {
        this(null, spinner);
    }

    public void setValue(int value) {
        if(slider != null) slider.setValue(value);
        if(spinner != null) spinner.setValue(value);
    }

    public int getValue() {
        if(slider != null) return slider.getValue();
        if(spinner != null) return (int) spinner.getValue();
        return -1;
    }

    public JSlider getSlider() {
        return slider;
    }

    public JSpinner getSpinner() {
        return spinner;
    }
}
