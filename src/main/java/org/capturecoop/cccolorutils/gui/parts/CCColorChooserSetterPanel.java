package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CCColorChooserSetterPanel extends JPanel {
    private Color color;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private ArrayList<ChangeListener> sliderUpdateListeners = new ArrayList<>();
    private ArrayList<ChangeListener> visualUpdateListeners = new ArrayList<>();
    private enum SLIDER_OPTION {RGB, HSB}

    public CCColorChooserSetterPanel(Color startColor) {
        this.color = startColor;

        setLayout(new GridLayout(0, 2));

        //Picker elements
        CCHSBPicker picker = new CCHSBPicker(color, true);
        CCHSBHueBar hueBar = new CCHSBHueBar(color, CCColorUtils.DIRECTION.VERTICAL, true);
        CCAlphaBar alphaBar = new CCAlphaBar(color, CCColorUtils.DIRECTION.VERTICAL, true);

        //Set size
        picker.setPreferredSize(new Dimension(256, 256));
        hueBar.setPreferredSize(new Dimension(32, 256));
        alphaBar.setPreferredSize(new Dimension(32, 256));

        visualUpdateListeners.add(e -> {
            CCHSB hsb = new CCHSB(color);
            picker.setHue(hsb.getHue());
            picker.setSaturation(hsb.getSaturation());
            picker.setBrightness(hsb.getBrightness());

            hueBar.setHue(hsb.getHue());

            alphaBar.setBackgroundColor(color);
            alphaBar.setAlpha(color.getAlpha());
        });

        //Add visuals
        JPanel visual = new JPanel();
        visual.add(picker);
        visual.add(hueBar);
        visual.add(alphaBar);

        //Add sliders
        JPanel sliderPanel = new JPanel(new GridBagLayout());
        setupRGBSliders(sliderPanel);

        //Add Listeners
        picker.addChangeListener(e -> {
            setColor(picker.getAsColor());
            alphaBar.setBackgroundColor(color);
            updateSliderListeners();
        });

        hueBar.addChangeListener(e -> {
            float hue = hueBar.getHue();
            picker.setHue(hue);
            alphaBar.setBackgroundColor(color);
            setColor(picker.getAsColor());
            updateSliderListeners();
        });

        alphaBar.addChangeListener(e -> {
            setColor(CCColorUtils.setColorAlpha(color, alphaBar.getAlpha()));
            updateSliderListeners();
        });

        updateSliderListeners();

        JPanel sliderOuterPanel = new JPanel();
        sliderOuterPanel.add(sliderPanel);

        JTabbedPane options = new JTabbedPane();
        options.addTab("RGB", sliderOuterPanel);

        add(visual);
        add(options);
    }

    public void setupRGBSliders(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);

        AtomicBoolean isSetter = new AtomicBoolean(false);

        gbc.gridx = 0;
        panel.add(new JLabel("Red"), gbc);
        gbc.gridx = 1;
        JSlider redSlider = createSlider(0, 255);
        redSlider.addChangeListener(e -> {
            if(!isSetter.get()) {
                setColor(CCColorUtils.setColorRed(color, redSlider.getValue()));
                updateVisualListeners();
            }
        });
        panel.add(redSlider, gbc);

        gbc.gridx = 0;
        panel.add(new JLabel("Green"), gbc);
        gbc.gridx = 1;
        JSlider greenSlider = createSlider(0, 255);
        greenSlider.addChangeListener(e -> {
            if(!isSetter.get()) {
                setColor(CCColorUtils.setColorGreen(color, greenSlider.getValue()));
                updateVisualListeners();
            }
        });
        panel.add(greenSlider, gbc);

        gbc.gridx = 0;
        panel.add(new JLabel("Blue"), gbc);
        gbc.gridx = 1;
        JSlider blueSlider = createSlider(0, 255);
        blueSlider.addChangeListener(e -> {
            if(!isSetter.get()) {
                setColor(CCColorUtils.setColorBlue(color, blueSlider.getValue()));
                updateVisualListeners();
            }
        });
        panel.add(blueSlider, gbc);

        gbc.gridx = 0;
        panel.add(new JLabel("Alpha"), gbc);
        gbc.gridx = 1;
        JSlider alphaSlider = createSlider(0, 255);
        alphaSlider.addChangeListener(e -> {
            if(!isSetter.get()) {
                setColor(CCColorUtils.setColorAlpha(color, alphaSlider.getValue()));
                updateVisualListeners();
            }
        });
        panel.add(alphaSlider, gbc);

        sliderUpdateListeners.add(e -> {
            isSetter.set(true);
            redSlider.setValue(color.getRed());
            greenSlider.setValue(color.getGreen());
            blueSlider.setValue(color.getBlue());
            alphaSlider.setValue(color.getAlpha());
            isSetter.set(false);
        });
    }

    public void updateSliderListeners() {
        for(ChangeListener listener : sliderUpdateListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    public void updateVisualListeners() {
        for(ChangeListener listener : visualUpdateListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    public JSlider createSlider(int min, int max) {
        JSlider slider = new JSlider(min, max);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setPreferredSize(new Dimension(240, 16));
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
