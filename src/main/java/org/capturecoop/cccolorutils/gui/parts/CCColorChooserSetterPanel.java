package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class CCColorChooserSetterPanel extends JPanel {
    private Color color;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

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

        //Visual elements
        JPanel visual = new JPanel();
        add(visual);

        //Sliders
        JPanel sliders = new JPanel();
        sliders.setLayout(new GridBagLayout());
        add(sliders);

        //Add visuals
        visual.add(picker);
        visual.add(hueBar);
        visual.add(alphaBar);

        //Add sliders
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        gbc.gridx = 0;
        sliders.add(new JLabel("Red"), gbc);
        gbc.gridx = 1;
        sliders.add(new JSlider(), gbc);

        gbc.gridx = 0;
        sliders.add(new JLabel("Green"), gbc);
        gbc.gridx = 1;
        sliders.add(new JSlider(), gbc);

        gbc.gridx = 0;
        sliders.add(new JLabel("Blue"), gbc);
        gbc.gridx = 1;
        sliders.add(new JSlider(), gbc);
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
