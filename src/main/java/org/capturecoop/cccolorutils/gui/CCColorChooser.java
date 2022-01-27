package org.capturecoop.cccolorutils.gui;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.gui.parts.CCColorChooserPreviewPanel;
import org.capturecoop.cccolorutils.gui.parts.CCColorChooserSetterPanel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CCColorChooser extends JFrame {
    private final CCColorChooser instance;
    private CCColor color;
    private CCColorChooserPreviewPanel previewPanel;
    private CCColorChooserSetterPanel setterPanel;

    private ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public CCColorChooser(CCColor color, String title, int x, int y, boolean useGradient, BufferedImage backgroundImage, BufferedImage icon) {
        instance = this;
        this.color = color;
        setterPanel = new CCColorChooserSetterPanel(color.getPrimaryColor());
        previewPanel = new CCColorChooserPreviewPanel(this, useGradient, backgroundImage);

        setTitle(title);
        if(icon != null) setIconImage(icon);
        init(x, y, useGradient, backgroundImage);
    }

    public void init(int x, int y, boolean useGradient, BufferedImage previewBackground) {
        JPanel mainPanel = new JPanel();
        JPanel submitButtonPanel = new JPanel();
        JButton submit = new JButton("Okay");
        submit.addActionListener(e -> instance.close());

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(previewPanel);
        mainPanel.add(setterPanel);
        mainPanel.add(submitButtonPanel);

        add(mainPanel);
        setFocusable(true);

        setAlwaysOnTop(true);

        pack();
        submit.setPreferredSize(new Dimension(this.getWidth()/2, 50));
        submitButtonPanel.add(submit);

        pack();
        setLocation(x - getWidth()/2, y - getHeight()/2);
        setVisible(true);
    }

    public CCColorChooserPreviewPanel getPreviewPanel() {
        return previewPanel;
    }

    public CCColorChooserSetterPanel getSetterPanel() {
        return setterPanel;
    }

    public CCColor getColor() {
        return color;
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void close() {
        dispose();
    }
}
