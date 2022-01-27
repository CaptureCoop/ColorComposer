package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.gui.CCColorChooser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CCColorChooserPreviewPanel extends JPanel{
    CCColorChooserSinglePreview panelSingle;
    CCColorChooserGradientPreview panelGradient;
    JTabbedPane tabPane;

    public CCColorChooserPreviewPanel(CCColorChooser colorChooser, boolean useGradient, BufferedImage previewBackground) {
        setPreferredSize(new Dimension(0, 256));
        setLayout(new GridLayout());
        tabPane = new JTabbedPane(JTabbedPane.TOP);

        panelSingle = new CCColorChooserSinglePreview(colorChooser, previewBackground);

        if(useGradient)
            panelGradient = new CCColorChooserGradientPreview(colorChooser, previewBackground);

        colorChooser.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                /*if(tabPane.getSelectedIndex() == 0)
                    colorChooser.getColor().setPrimaryColor(colorChooser.getColor());
                else if(tabPane.getSelectedIndex() == 1)
                    panelGradient.setColorAuto(colorChooser.getJcc().getColor());*/
                //TODO: What is this?
            }
        });
        tabPane.addTab("Single color", panelSingle);
        if(useGradient)
            tabPane.addTab("Gradient",  panelGradient);

        if(colorChooser.getColor().isGradient() && useGradient)
            tabPane.setSelectedIndex(1);

        tabPane.addChangeListener(e -> {
            switch(tabPane.getSelectedIndex()) {
                case 0: colorChooser.getColor().setIsGradient(false); break;
                case 1: colorChooser.getColor().setIsGradient(true); break;
            }
        });
        add(tabPane);
    }
}
