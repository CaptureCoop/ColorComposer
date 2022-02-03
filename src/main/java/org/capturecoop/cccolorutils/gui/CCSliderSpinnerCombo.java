package org.capturecoop.cccolorutils.gui;

import javax.swing.*;

public class CCSliderSpinnerCombo {
    private final JSlider slider;
    private final JSpinner spinner;

    public CCSliderSpinnerCombo(JSlider slider, JSpinner spinner) {
        this.slider = slider;
        this.spinner = spinner;
    }

    public void setValue(int value) {
        slider.setValue(value);
        spinner.setValue(value);
    }

    public JSlider getSlider() {
        return slider;
    }

    public JSpinner getSpinner() {
        return spinner;
    }
}
