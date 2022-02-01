package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;
import org.capturecoop.cccolorutils.gui.ISliderUpdate;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CCColorChooserSetterPanel extends JPanel {
    private Color color;
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private final ArrayList<ChangeListener> sliderUpdateListeners = new ArrayList<>();
    private final ArrayList<ChangeListener> visualUpdateListeners = new ArrayList<>();
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

        //Add Listeners
        picker.addChangeListener(e -> {
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
            alphaBar.setBackgroundColor(color);
            updateSliderListeners();
        });

        hueBar.addChangeListener(e -> {
            float hue = hueBar.getHue();
            picker.setHue(hue);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
            alphaBar.setBackgroundColor(color);
            updateSliderListeners();
        });

        alphaBar.addChangeListener(e -> {
            setColor(CCColorUtils.setColorAlpha(color, alphaBar.getAlpha()), true, false);
            updateSliderListeners();
        });

        JPanel rgbPanel = new JPanel();
        rgbPanel.add(setupRGBSliders(new JPanel(new GridBagLayout())));
        JPanel hsbPanel = new JPanel();
        hsbPanel.add(setupHSBSliders(new JPanel(new GridBagLayout()), picker, hueBar, alphaBar));


        JTabbedPane options = new JTabbedPane();
        options.addTab("RGB", rgbPanel);
        options.addTab("HSB", hsbPanel);

        add(visual);
        add(options);
        updateSliderListeners();
    }

    public JPanel setupHSBSliders(JPanel panel, CCHSBPicker picker, CCHSBHueBar hueBar, CCAlphaBar alphaBar) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        AtomicBoolean isSetter = new AtomicBoolean(false);

        JSlider hueSlider = createSlider(panel, "Hue", 0, 100, isSetter, gbc, slider -> {
            float hue = slider.getValue() / 100F;
            picker.setHue(hue);
            hueBar.setHue(hue);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
        });

        JSlider saturationSlider = createSlider(panel, "Saturation", 0, 100, isSetter, gbc, slider -> {
            picker.setSaturation(slider.getValue() / 100F);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
        });

        JSlider brightnessSlider = createSlider(panel, "Brightness", 0, 100, isSetter, gbc, slider -> {
            picker.setBrightness(slider.getValue() / 100F);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
        });

        JSlider alphaSlider = createSlider(panel, "Alpha", 0, 255, isSetter, gbc, slider -> {
            setColor(CCColorUtils.setColorAlpha(color, slider.getValue()), true, false);
            updateVisualListeners();
        });

        sliderUpdateListeners.add(e -> {
            isSetter.set(true);
            hueSlider.setValue((int)(picker.getHue() * 100F));
            saturationSlider.setValue((int)(picker.getSaturation() * 100F));
            brightnessSlider.setValue((int)(picker.getBrightness() * 100F));
            alphaSlider.setValue(color.getAlpha());
            isSetter.set(false);
        });

        return panel;
    }

    public JSlider createSlider(JPanel panel, String title, int min, int max, AtomicBoolean isSetter, GridBagConstraints gbc, ISliderUpdate onChange) {
        JSlider slider = new JSlider(min, max);
        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setPreferredSize(new Dimension(240, 16));
        slider.addChangeListener(e -> {
            if(!isSetter.get()) {
                onChange.update(slider);
            }
        });
        
        gbc.gridx = 0;
        panel.add(new JLabel(title), gbc);
        gbc.gridx = 1;
        panel.add(slider, gbc);
        return slider;
    }

    public JPanel setupRGBSliders(JPanel panel) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        AtomicBoolean isSetter = new AtomicBoolean(false);

        JSlider redSlider = createSlider(panel, "Red", 0, 255, isSetter, gbc, slider -> {
            setColor(CCColorUtils.setColorRed(color, slider.getValue()), true, false);
            updateVisualListeners();
        });

        JSlider greenSlider = createSlider(panel, "Green", 0, 255, isSetter, gbc, slider -> {
            setColor(CCColorUtils.setColorGreen(color, slider.getValue()), true, false);
            updateVisualListeners();
        });

        JSlider blueSlider = createSlider(panel, "Blue", 0, 255, isSetter, gbc, slider -> {
            setColor(CCColorUtils.setColorBlue(color, slider.getValue()), true, false);
            updateVisualListeners();
        });

        JSlider alphaSlider = createSlider(panel, "Alpha", 0, 255, isSetter, gbc, slider -> {
            setColor(CCColorUtils.setColorAlpha(color, slider.getValue()), true, false);
            updateVisualListeners();
        });

        sliderUpdateListeners.add(e -> {
            isSetter.set(true);
            redSlider.setValue(color.getRed());
            greenSlider.setValue(color.getGreen());
            blueSlider.setValue(color.getBlue());
            alphaSlider.setValue(color.getAlpha());
            isSetter.set(false);
        });
        return panel;
    }

    public void updateSliderListeners() {
        for(ChangeListener listener : sliderUpdateListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    public void updateVisualListeners() {
        for(ChangeListener listener : visualUpdateListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void setColor(Color color, boolean alertListeners, boolean updateComponents) {
        this.color = color;
        if(alertListeners)
            for(ChangeListener listener : changeListeners)
                listener.stateChanged(new ChangeEvent(this));

        if (updateComponents) {
            for(ChangeListener listener : visualUpdateListeners)
                listener.stateChanged(new ChangeEvent(this));

            for(ChangeListener listener : sliderUpdateListeners)
                listener.stateChanged(new ChangeEvent(this));
        }
    }

    public Color getColor() {
        return color;
    }
}
