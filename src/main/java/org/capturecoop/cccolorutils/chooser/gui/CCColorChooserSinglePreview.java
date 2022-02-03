package org.capturecoop.cccolorutils.chooser.gui;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.chooser.CCColorChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CCColorChooserSinglePreview extends JPanel {
    private final CCColor color;
    private final BufferedImage previewBackground;

    public CCColorChooserSinglePreview(CCColorChooser colorChooser, BufferedImage previewBackground) {
        color = colorChooser.getColor();
        color.addChangeListener(e -> repaint());
        this.previewBackground = previewBackground;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(previewBackground != null)
            g.drawImage(previewBackground.getSubimage(0, 0, getHeight(), getHeight()), getWidth() / 2 - getHeight()/2, 0, getHeight(), getHeight(), null);
        if(color != null) {
            g.setColor(color.getPrimaryColor());
            g.fillRect(getWidth() / 2 - getHeight()/2, 0, getHeight(), getHeight());
        }
    }
}
