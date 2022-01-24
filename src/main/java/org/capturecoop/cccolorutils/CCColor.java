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

    @Override
    public String toString() {
        return CCStringUtils.format("CCColor[R: %c, G: %c, B: %c]", color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        CCColor other = (CCColor) o;

        return new EqualsBuilder()
                .append(color.getRGB(), other.color.getRGB())
                .append(color.getAlpha(), other.color.getAlpha())
                .isEquals();
    }
}
