package org.capturecoop.cccolorutils.gui;

import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;
import org.capturecoop.ccutils.math.CCVector2Float;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CCHSBPicker extends JPanel {
    private float hue;
    private float saturation;
    private float brightness;

    private boolean isDragging = false;

    private BufferedImage buffer;
    private boolean dirty = true;

    private static final int MARGIN = 10;
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public CCHSBPicker(Color color, boolean alwaysGrab) {
        CCHSB hsb = new CCHSB(color);
        hue = hsb.getHue();
        saturation = hsb.getSaturation();
        brightness = hsb.getBrightness();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Rectangle rect = getSelectRect();

                if(rect.contains(mouseEvent.getPoint()) && !alwaysGrab)
                    isDragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                isDragging = false;
                if(alwaysGrab)
                    execute(mouseEvent.getX(), mouseEvent.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if(isDragging || alwaysGrab)
                    execute(mouseEvent.getX(), mouseEvent.getY());
            }
        });
    }

    private void execute(int x, int y) {
        dirty = true;
        float percentageX = (x * 100F) / getWidth();
        float percentageY = (y * 100F) / getHeight();
        float pointX = new CCVector2Float(percentageX / 100F, 0).limitX(0F, 1F).getX();
        float pointY = new CCVector2Float(percentageY / 100F, 0).limitX(0F, 1F).getX();
        pointY = (pointY - 1) * - 1;
        saturation = pointX;
        brightness = pointY;
        repaint();

        for(ChangeListener listener : changeListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    private Rectangle getSelectRect() {
        int posX = (int)(saturation * getSizeX());
        int posY = (int)((brightness - 1) * - 1 * getSizeY());
        return new Rectangle(posX, posY, MARGIN, MARGIN);
    }

    private int getSizeX() {
        return getWidth() - MARGIN;
    }

    private int getSizeY() {
        return getHeight() - MARGIN;
    }

    @Override
    public void paint(Graphics g) {
        if(!dirty && buffer != null) {
            g.drawImage(buffer, 0, 0, this);
            return;
        }

        if(buffer == null || !(buffer.getWidth() == getWidth() && buffer.getHeight() == getHeight())) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        }

        Graphics bufferGraphics = buffer.getGraphics();
        dirty = false;

        int sizeX = getSizeX();
        int sizeY = getSizeY();
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
        bufferGraphics.drawImage(CCColorUtils.createHSVBox(getWidth(), getHeight(), hue), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this);
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawRect(MARGIN / 2 - 1, MARGIN / 2 - 1, sizeX + 1, sizeY + 1);
        bufferGraphics.setColor(Color.GRAY);
        Rectangle rect = getSelectRect();
        bufferGraphics.fillRect(rect.x, rect.y, rect.width, rect.height);

        bufferGraphics.dispose();
        g.drawImage(buffer, 0, 0, this);
    }

    public void addChangeListener(ChangeListener listener) {
        //These only fire on changes made by the user
        changeListeners.add(listener);
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        this.hue = hue;
        dirty = true;
        repaint();
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
        dirty = true;
        repaint();
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
        dirty = true;
        repaint();
    }

    public Color getAsColor() {
        return new CCHSB(hue, saturation, brightness).toRGB();
    }
}
