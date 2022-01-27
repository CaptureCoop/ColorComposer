package org.capturecoop.cccolorutils.gui.parts;

import org.capturecoop.cccolorutils.CCColorUtils;
import org.capturecoop.ccutils.math.CCVector2Float;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class CCAlphaBar extends JPanel {
    private Color backgroundColor;
    private float alpha;

    private final CCColorUtils.DIRECTION direction;

    private final static int MARGIN = 10;
    private final static int SEL_MARGIN = 4;
    private final static int SEL_MARGIN_OFF = 2;

    private static BufferedImage gridImage;

    private boolean isDragging = false;

    private BufferedImage buffer;
    private boolean dirty = true;
    private final ArrayList<ChangeListener> changeListeners = new ArrayList<>();

    public CCAlphaBar(Color color, CCColorUtils.DIRECTION direction, boolean alwaysGrab) {
        backgroundColor = color;
        this.direction = direction;
        alpha = color.getAlpha() / 255F;

        if(gridImage == null) {
            try {
                gridImage = ImageIO.read(CCAlphaBar.class.getResource("/org/capturecoop/cccolorutils/transparent_grid_4x4.png"));
            } catch (IOException ioException) {
                //Do nothing, that image is not essential.
            }
        }

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
        alpha = new CCVector2Float(percentage / 100F, 0).limitX(0, 1).getX();
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
                int yPos = (int) (getSizeY() / (1 / alpha)) + MARGIN / 2;
                return new Rectangle(SEL_MARGIN_OFF, yPos - SEL_MARGIN, getWidth() - SEL_MARGIN_OFF * 2, SEL_MARGIN * 2);
            case HORIZONTAL:
                int xPos = (int) (getSizeX() / (1 / alpha)) + MARGIN / 2;
                return new Rectangle(xPos - SEL_MARGIN, SEL_MARGIN_OFF, SEL_MARGIN * 2, getHeight() - SEL_MARGIN_OFF * 2);
        }
        return null;
    }

    @Override
    public void paint(Graphics g) {
        if(buffer == null || !(buffer.getWidth() == getWidth() && buffer.getHeight() == getHeight())) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        int sizeX = getSizeX();
        int sizeY = getSizeY();
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        if (dirty) {
            Graphics bufferGraphics = buffer.getGraphics();
            if (gridImage != null) {
                if (direction == CCColorUtils.DIRECTION.HORIZONTAL) {
                    int amount = sizeX / sizeY;
                    for (int i = 0; i < amount; i++) {
                        bufferGraphics.drawImage(gridImage, MARGIN / 2 + i * sizeY, MARGIN / 2, sizeY, sizeY, this);
                    }
                } else {
                    int amount = sizeY / sizeX;
                    for (int i = 0; i < amount; i++) {
                        bufferGraphics.drawImage(gridImage, MARGIN / 2, MARGIN / 2 + i * sizeX, sizeX, sizeX, this);
                    }
                }
            }
            bufferGraphics.drawImage(CCColorUtils.createAlphaBar(backgroundColor, sizeX, sizeY, direction), MARGIN / 2, MARGIN / 2, sizeX, sizeY, this);
            bufferGraphics.dispose();
            dirty = false;
        }
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

    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        dirty = true;
        repaint();
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha / 255F;
        dirty = true;
        repaint();
    }

    public int getAlpha() {
        return (int) (alpha * 255F);
    }
}
