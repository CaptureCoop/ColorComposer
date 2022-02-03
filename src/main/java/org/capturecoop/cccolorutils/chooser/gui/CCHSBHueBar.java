package org.capturecoop.cccolorutils.chooser.gui;

import org.capturecoop.cccolorutils.CCColor;
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

public class CCHSBHueBar extends JPanel {
    private float hue;
    private final CCColorUtils.DIRECTION direction;

    private final static int MARGIN = 10;
    private final static int SEL_MARGIN = 4;
    private final static int SEL_MARGIN_OFF = 2;
    private boolean isDragging = false;

    private BufferedImage buffer;
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public CCHSBHueBar(Color color, CCColorUtils.DIRECTION direction, boolean alwaysGrab) {
        hue = new CCHSB(color).getHue();
        this.direction = direction;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Rectangle rect = getSelectRect();
                if(rect == null)
                    return;

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
        int pos = y;
        int size = getHeight();
        if(direction == CCColorUtils.DIRECTION.HORIZONTAL) {
            pos = x;
            size = getWidth();
        }
        float percentage = (pos * 100F) / size;
        hue = new CCVector2Float(percentage / 100F, 0).limitX(0.0001F, 0.9999F).getX();
        repaint();

        for(ChangeListener listener : changeListeners)
            listener.stateChanged(new ChangeEvent(this));
    }

    private int getSizeX() {
        return getWidth() - MARGIN;
    }

    private int getSizeY() {
        return getHeight() - MARGIN;
    }

    private Rectangle getSelectRect() {
        switch(direction) {
            case VERTICAL:
                int yPos = (int) (getSizeY() / (1 / hue)) + MARGIN / 2;
                return new Rectangle(SEL_MARGIN_OFF, yPos - SEL_MARGIN, getWidth() - SEL_MARGIN_OFF * 2, SEL_MARGIN * 2);
            case HORIZONTAL:
                int xPos = (int) (getSizeX() / (1 / hue)) + MARGIN / 2;
                return new Rectangle(xPos - SEL_MARGIN, SEL_MARGIN_OFF, SEL_MARGIN * 2, getHeight() - SEL_MARGIN_OFF * 2);
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        int sizeX = getSizeX();
        int sizeY = getSizeY();

        if(buffer == null || !(buffer.getWidth() == getWidth() && buffer.getHeight() == getHeight())) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics bufferGraphics = buffer.getGraphics();
            bufferGraphics.drawImage(CCColorUtils.createHSVHueBar(sizeX, sizeY, direction), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this);
            bufferGraphics.dispose();
        }

        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(buffer, 0, 0, this);
        g.setColor(Color.BLACK);
        g.drawRect(MARGIN / 2 - 1, MARGIN / 2 - 1, sizeX + 1, sizeY + 1);
        g.setColor(Color.GRAY);
        Rectangle rect = getSelectRect();
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
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
        repaint();
    }
}
