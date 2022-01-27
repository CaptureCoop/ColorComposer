package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class CCColorChooserSetterPanel extends JPanel {
    private Color color;
    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public CCColorChooserSetterPanel(Color color) {
        this.color = color;
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
