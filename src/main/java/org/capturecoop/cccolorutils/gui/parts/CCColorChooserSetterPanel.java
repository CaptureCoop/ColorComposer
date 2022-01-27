package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class CCColorChooserSetterPanel extends JPanel {
    private Color color;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private enum SLIDER_OPTION {RGB, HSB}

    public CCColorChooserSetterPanel(Color color) {
        this.color = color;

        //Picker elements
        CCHSBPicker picker = new CCHSBPicker(color, true);
        CCHSBHueBar hueBar = new CCHSBHueBar(color, CCColorUtils.DIRECTION.VERTICAL, true);
        CCAlphaBar alphaBar = new CCAlphaBar(color, CCColorUtils.DIRECTION.VERTICAL, true);

        //Set size
        picker.setPreferredSize(new Dimension(256, 256));
        hueBar.setPreferredSize(new Dimension(32, 256));
        alphaBar.setPreferredSize(new Dimension(32, 256));

        setLayout(new GridLayout(0, 2));

        //Add visuals
        JPanel visual = new JPanel();
        visual.add(picker);
        visual.add(hueBar);
        visual.add(alphaBar);

        //Add sliders
        JPanel sliderPanel = new JPanel(new GridBagLayout());
        setupSliders(SLIDER_OPTION.RGB, sliderPanel);

        JPanel optionPanel = new JPanel();
        optionPanel.add(sliderPanel);

        add(visual);
        add(optionPanel);
    }

    public void setupSliders(SLIDER_OPTION type, JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);

        panel.removeAll();

        gbc.gridx = 0;
        panel.add(new JLabel("Red"), gbc);
        gbc.gridx = 1;
        panel.add(createSlider(e -> System.out.println("Red")), gbc);

        gbc.gridx = 0;
        panel.add(new JLabel("Green"), gbc);
        gbc.gridx = 1;
        panel.add(createSlider(e -> System.out.println("Green")), gbc);

        gbc.gridx = 0;
        panel.add(new JLabel("Blue"), gbc);
        gbc.gridx = 1;
        panel.add(createSlider(e -> System.out.println("Blue")), gbc);
        panel.revalidate();
    }

    public JSlider createSlider(ChangeListener onChange) {
        JSlider slider = new JSlider();
        slider.setPreferredSize(new Dimension(240, 16));
        slider.addChangeListener(onChange);
        return slider;
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
