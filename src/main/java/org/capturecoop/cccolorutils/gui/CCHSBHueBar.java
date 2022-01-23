package org.capturecoop.cccolorutils.gui;

import org.capturecoop.cccolorutils.CCColor;
import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.cccolorutils.CCHSB;
import org.capturecoop.ccutils.math.CCVector2Float;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class CCHSBHueBar extends JPanel {
    private CCColor color;
    private float position;
    private final CCColorUtils.DIRECTION direction;

    private final static int MARGIN = 10;
    private final static int SEL_MARGIN = 4;
    private final static int SEL_MARGIN_OFF = 2;
    private boolean isDragging = false;

    private BufferedImage buffer;
    private boolean dirty = true;

    public CCHSBHueBar(CCColor color, CCColorUtils.DIRECTION direction, boolean alwaysGrab) {
        this.color = color;
        this.direction = direction;
        updateHSV();
        color.addChangeListener(e -> updateHSV());
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
        dirty = true;
        int pos = y;
        int size = getHeight();
        if(direction == CCColorUtils.DIRECTION.HORIZONTAL) {
            pos = x;
            size = getWidth();
        }
        float percentage = (pos * 100F) / size;
        position = new CCVector2Float(percentage / 100F, 0).limitX(0.01F, 0.99F).getX();
        CCHSB current = new CCHSB(color.getPrimaryColor());
        color.setPrimaryColor(new CCHSB(position, current.getSaturation(), current.getBrightness(), current.getAlpha()).toRGB());
        repaint();

    }

    private void updateHSV() {
        if(!isDragging) {
            dirty = true;
            position = new CCHSB(color.getPrimaryColor()).getHue();
        }
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
                int yPos = (int) (getSizeY() / (1 / position)) + MARGIN / 2;
                return new Rectangle(SEL_MARGIN_OFF, yPos - SEL_MARGIN, getWidth() - SEL_MARGIN_OFF * 2, SEL_MARGIN * 2);
            case HORIZONTAL:
                int xPos = (int) (getSizeX() / (1 / position)) + MARGIN / 2;
                return new Rectangle(xPos - SEL_MARGIN, SEL_MARGIN_OFF, SEL_MARGIN * 2, getHeight() - SEL_MARGIN_OFF * 2);
        }
        return null;
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
        bufferGraphics.drawImage(CCColorUtils.createHSVHueBar(sizeX, sizeY, direction), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this);
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawRect(MARGIN / 2 - 1, MARGIN / 2 - 1, sizeX + 1, sizeY + 1);
        bufferGraphics.setColor(Color.GRAY);
        Rectangle rect = getSelectRect();
        bufferGraphics.fillRect(rect.x, rect.y, rect.width, rect.height);

        bufferGraphics.dispose();
        g.drawImage(buffer, 0, 0, this);
    }
}
