package org.capturecoop.cccolorutils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.capturecoop.ccutils.utils.CCStringUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class CCColor {
    private Color color;
    enum VALUE_TYPE {RED, GREEN, BLUE, ALPHA}
    private final ArrayList<ChangeListener> listeners = new ArrayList<>();

    public CCColor(Color color) {
        this.color = color;
    }

    public CCColor() {
        this(Color.WHITE);
    }

    public CCColor(CCColor color) {
        this(color.color);
    }

    public CCColor(int r, int g, int b, int a) {
        this(new Color(r, g, b, a));
    }

    public void setAlpha(int alpha) {
        Color oldColor = color;
        color = new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), alpha);
    }

    public void setColor(Color color) {
        this.color = color;
        alertChangeListeners();
    }

    private void alertChangeListeners() {
        for(ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public int getRed() {
        return color.getRed();
    }

    public int getGreen() {
        return color.getGreen();
    }

    public int getBlue() {
        return color.getBlue();
    }

    public int getAlpha() {
        return color.getAlpha();
    }

    public int getRGB() {
        return color.getRGB();
    }

    public Color getRawColor() {
        return color;
    }

    @Override
    public String toString() {
        return CCStringUtils.format("CCColor[R: %c, G: %c, B: %c]", color.getRed(), color.getGreen(), color.getBlue());
    }

    public boolean advancedEquals(Object o, VALUE_TYPE... toCompare) {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        CCColor other = (CCColor) o;

        if(toCompare == null || toCompare.length == 0) {
            return new EqualsBuilder()
                    .append(color.getRGB(), other.color.getRGB())
                    .append(color.getAlpha(), other.color.getAlpha())
                    .isEquals();
        }

        for(VALUE_TYPE type : toCompare) {
            switch (type) {
                case RED: if(color.getRed() != other.color.getRed()) return false;
                case GREEN: if(color.getGreen() != other.color.getGreen()) return false;
                case BLUE: if(color.getBlue() != other.color.getBlue()) return false;
                case ALPHA: if(color.getAlpha() != other.color.getAlpha()) return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        return advancedEquals(o);
    }
}
