package org.capturecoop.cccolorutils.gui;

import javax.swing.*;

public class CCSliderOrSpinner {
    private final JComponent component;
    private final TYPE type;
    private enum TYPE {SLIDER, SPINNER}

    public CCSliderOrSpinner(JSlider slider) {
        component = slider;
        type = TYPE.SLIDER;
    }

    public CCSliderOrSpinner(JSpinner spinner) {
        component = spinner;
        type = TYPE.SPINNER;
    }

    public void setValue(int value) {
        switch (type) {
            case SLIDER: ((JSlider)component).setValue(value); return;
            case SPINNER: ((JSpinner)component).setValue(value); return;
        }
    }

    public int getValue() {
        switch (type) {
            case SLIDER: return ((JSlider)component).getValue();
            case SPINNER: return (int)((JSpinner)component).getValue();
        }
        return -1;
    }
}
