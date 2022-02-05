package org.capturecoop.cccolorutils.chooser;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.chooser.gui.CCColorChooserPreviewPanel;
import org.capturecoop.cccolorutils.chooser.gui.CCColorChooserSetterPanel;
import org.capturecoop.ccutils.utils.ICCClosable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CCColorChooser extends JFrame implements ICCClosable {
    private final CCColorChooser instance;
    private CCColor color;
    private final CCColorChooserPreviewPanel previewPanel;
    private final CCColorChooserSetterPanel setterPanel;

    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();
    private final ChangeListener colorChangeListener;

    public CCColorChooser(CCColor color, String title, int x, int y, boolean useGradient, BufferedImage backgroundImage, BufferedImage icon) {
        instance = this;
        this.color = color;
        setterPanel = new CCColorChooserSetterPanel(color.getPrimaryColor(), this);
        previewPanel = new CCColorChooserPreviewPanel(this, useGradient, backgroundImage);

        colorChangeListener = e -> alertChangeListeners();
        color.addChangeListener(colorChangeListener);

        setTitle(title);
        if(icon != null) setIconImage(icon);
        init(x, y);
    }

    public void init(int x, int y) {
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

    public CCColorChooserSetterPanel getSetterPanel() {
        return setterPanel;
    }

    public CCColor getColor() {
        return color;
    }

    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(listener);
    }

    public void alertChangeListeners() {
        for(ChangeListener listener : changeListeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void close() {
        color.removeChangeListener(colorChangeListener);
        dispose();
    }
}
