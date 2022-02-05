package org.capturecoop.cccolorutils.chooser.gui;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;
import org.capturecoop.cccolorutils.chooser.CCColorChooser;
import org.capturecoop.cccolorutils.chooser.CCSetterManualCombo;
import org.capturecoop.cccolorutils.chooser.CCISetterManualUpdate;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class CCColorChooserSetterPanel extends JPanel {
    private final CCColorChooser chooser;
    private Color color;
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private final ArrayList<ChangeListener> sliderUpdateListeners = new ArrayList<>();
    private final ArrayList<ChangeListener> visualUpdateListeners = new ArrayList<>();

    public CCColorChooserSetterPanel(Color startColor, CCColorChooser chooser) {
        this.color = startColor;
        this.chooser = chooser;

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
        gbc.insets = new Insets(5, 5, 5, 5);
        AtomicBoolean isSetter = new AtomicBoolean(false);

        CCSetterManualCombo hue = createSettings(panel, "Hue", 0, 100, isSetter, gbc, component -> {
            float hueValue = component.getValue() / 100F;
            picker.setHue(hueValue);
            hueBar.setHue(hueValue);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo saturationSlider = createSettings(panel, "Saturation", 0, 100, isSetter, gbc, component -> {
            picker.setSaturation(component.getValue() / 100F);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo brightnessSlider = createSettings(panel, "Brightness", 0, 100, isSetter, gbc, component -> {
            picker.setBrightness(component.getValue() / 100F);
            setColor(CCColorUtils.setColorAlpha(picker.getAsColor(), color.getAlpha()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo alphaSlider = createSettings(panel, "Alpha", 0, 255, isSetter, gbc, component -> {
            alphaBar.setAlpha(component.getValue());
            setColor(CCColorUtils.setColorAlpha(color, component.getValue()), true, false);
            updateVisualListeners();
        });

        createHexInput(panel, gbc);
        createCCColorFormatField(panel, gbc);

        sliderUpdateListeners.add(e -> {
            isSetter.set(true);
            hue.setValue((int)(picker.getHue() * 100F));
            saturationSlider.setValue((int)(picker.getSaturation() * 100F));
            brightnessSlider.setValue((int)(picker.getBrightness() * 100F));
            alphaSlider.setValue(color.getAlpha());
            isSetter.set(false);
        });
        return panel;
    }

    public JPanel setupRGBSliders(JPanel panel) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        AtomicBoolean isSetter = new AtomicBoolean(false);

        CCSetterManualCombo red = createSettings(panel, "Red", 0, 255, isSetter, gbc, component -> {
            setColor(CCColorUtils.setColorRed(color, component.getValue()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo green = createSettings(panel, "Green", 0, 255, isSetter, gbc, component -> {
            setColor(CCColorUtils.setColorGreen(color, component.getValue()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo blue = createSettings(panel, "Blue", 0, 255, isSetter, gbc, component -> {
            setColor(CCColorUtils.setColorBlue(color, component.getValue()), true, false);
            updateVisualListeners();
        });

        CCSetterManualCombo alpha = createSettings(panel, "Alpha", 0, 255, isSetter, gbc, component -> {
            setColor(CCColorUtils.setColorAlpha(color, component.getValue()), true, false);
            updateVisualListeners();
        });

        createHexInput(panel, gbc);
        createCCColorFormatField(panel, gbc);

        sliderUpdateListeners.add(e -> {
            isSetter.set(true);
            red.setValue(color.getRed());
            green.setValue(color.getGreen());
            blue.setValue(color.getBlue());
            alpha.setValue(color.getAlpha());
            isSetter.set(false);
        });

        return panel;
    }

    public CCSetterManualCombo createSettings(JPanel panel, String title, int min, int max, AtomicBoolean isSetter, GridBagConstraints gbc, CCISetterManualUpdate onUpdate) {
        JSlider slider = new JSlider(min, max);
        JSpinner spinner = new JSpinner();

        slider.setMinimum(min);
        slider.setMaximum(max);
        slider.setPreferredSize(new Dimension(240, 16));
        slider.addChangeListener(e -> {
            if(!isSetter.get()) {
                onUpdate.update(new CCSetterManualCombo(slider));
            }
            spinner.setValue(slider.getValue());
        });

        gbc.gridx = 0;
        panel.add(new JLabel(title), gbc);
        gbc.gridx = 1;
        panel.add(slider, gbc);

        spinner.addChangeListener(e -> {
            if(!isSetter.get()) {
                onUpdate.update(new CCSetterManualCombo(spinner));
            }
            slider.setValue((int)spinner.getValue());
        });
        spinner.setModel(new SpinnerNumberModel(0, min, max, 1));
        gbc.gridx = 2;
        panel.add(spinner, gbc);

        return new CCSetterManualCombo(slider, spinner);
    }

    public void createCCColorFormatField(JPanel panel, GridBagConstraints gbc) {
        JTextField textArea = new JTextField(chooser.getColor().toSaveString());
        Dimension size = textArea.getPreferredSize();
        size.width = size.width * 5;
        textArea.setPreferredSize(size);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_ESCAPE:
                        chooser.requestFocus();
                        chooser.setColor(CCColor.fromSaveString(textArea.getText()));
                        break;
                }
            }
        });

        chooser.getColor().addChangeListener(e -> textArea.setText(chooser.getColor().toSaveString()));

        gbc.gridx = 0;
        panel.add(new JLabel("Save String"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textArea, gbc);
    }

    public void createHexInput(JPanel panel, GridBagConstraints gbc) {
        JTextField textArea = new JTextField(CCColorUtils.rgb2hex(color));
        Dimension size = textArea.getPreferredSize();
        size.width = size.width * 2;
        textArea.setPreferredSize(size);
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_ESCAPE:
                        chooser.requestFocus();
                        Color temp = CCColorUtils.hex2rgb(textArea.getText());
                        if(temp != null)
                            setColor(temp, true, true);
                        else
                            textArea.setText(CCColorUtils.rgb2hex(color));
                        break;
                }
            }
        });
        ChangeListener update = e -> textArea.setText(CCColorUtils.rgb2hex(color));
        visualUpdateListeners.add(update);
        sliderUpdateListeners.add(update);

        gbc.gridx = 0;
        panel.add(new JLabel("HEX"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textArea, gbc);
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
