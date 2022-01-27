package org.capturecoop.cccolorutils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.capturecoop.ccutils.math.CCVector2Float;
import org.capturecoop.ccutils.math.CCVector2Int;
import org.capturecoop.ccutils.utils.CCStringUtils;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class CCColor {
    private Color primaryColor;
    private Color secondaryColor;
    private CCVector2Float point1;
    private CCVector2Float point2;
    private boolean isGradient = false;

    private final ArrayList<ChangeListener> listeners = new ArrayList<>();
    enum COLOR_TYPE {PRIMARY, SECONDARY}

    public static final CCVector2Float POINT_PRIMARY_DEFAULT = new CCVector2Float(0, 0);
    public static final CCVector2Float POINT_SECONDARY_DEFAULT = new CCVector2Float(1, 1);

    public CCColor(Color color) {
        this.primaryColor = color;
    }

    public CCColor() {
        this(Color.WHITE);
    }

    public CCColor(CCColor color) {
        primaryColor = color.primaryColor;
        secondaryColor = color.secondaryColor;
        isGradient = color.isGradient;
        if(color.point1 != null) point1 = new CCVector2Float(color.point1);
        if(color.point2 != null) point2 = new CCVector2Float(color.point2);
    }

    public CCColor(CCColor color, int alpha) {
        primaryColor = CCColorUtils.setColorAlpha(color.primaryColor, alpha);
        if(color.secondaryColor != null) {
            secondaryColor = CCColorUtils.setColorAlpha(color.secondaryColor, alpha);
        }
        isGradient = color.isGradient;
        if(color.point1 != null) point1 = new CCVector2Float(color.point1);
        if(color.point2 != null) point2 = new CCVector2Float(color.point2);
    }

    public CCColor(Color primaryColor, Color secondaryColor, boolean isGradient) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.isGradient = isGradient;
    }

    public CCColor(Color c, int alpha) {
        primaryColor = CCColorUtils.setColorAlpha(c, alpha);
    }

    public CCColor(int r, int g, int b, int a) {
        primaryColor = new Color(r, g, b, a);
    }

    public Color getColor(COLOR_TYPE type) {
        if (type == COLOR_TYPE.SECONDARY)
            return secondaryColor;
        return primaryColor;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setPrimaryColor(Color color, int alpha) {
        if(color == null) {
            setPrimaryColor(null);
            return;
        }
        setPrimaryColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public void setPrimaryColor(Color color) {
        primaryColor = color;
        alertChangeListeners();
    }

    public void setSecondaryColor(Color color, int alpha) {
        if(color == null) {
            setSecondaryColor(null);
            return;
        }
        setSecondaryColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
    }

    public void setSecondaryColor(Color color) {
        secondaryColor = color;
        alertChangeListeners();
    }

    public void setPoint1(CCVector2Float point) {
        if(point != null) {
            point1 = new CCVector2Float(point);
            point1.limit(0f, 1f);
        } else {
            point1 = null;
        }
        alertChangeListeners();
    }

    public void setPoint2(CCVector2Float point) {
        if(point != null) {
            point2 = new CCVector2Float(point);
            point2.limit(0f, 1f);
        } else {
            point2 = null;
        }
        alertChangeListeners();
    }

    public CCVector2Float getPoint1() {
        return point1;
    }

    public CCVector2Float getPoint2() {
        return point2;
    }

    public Paint getGradientPaint(int width, int height, int posX, int posY) {
        if(!isGradient) {
            return primaryColor;
        }

        ensureSecondaryColor();

        if(point1 == null)
            point1 = POINT_PRIMARY_DEFAULT;
        if(point2 == null)
            point2 = POINT_SECONDARY_DEFAULT;

        CCVector2Int point1int = new CCVector2Int(point1.getX() * width, point1.getY() * height);
        CCVector2Int point2int = new CCVector2Int(point2.getX() * width, point2.getY() * height);
        return new GradientPaint(point1int.getX() + posX, point1int.getY() + posY, primaryColor, point2int.getX() + posX, point2int.getY() + posY, secondaryColor);
    }

    public Paint getGradientPaint(int width, int height) {
        return getGradientPaint(width, height, 0, 0);
    }

    private void alertChangeListeners() {
        for(ChangeListener listener : listeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public String toSaveString() {
        String string = CCColorUtils.rgb2hex(primaryColor);
        if(primaryColor.getAlpha() != 255)
            string += "_a" + primaryColor.getAlpha();

        if(point1 != null && !point1.equals(POINT_PRIMARY_DEFAULT))
            string += "_x" + point1.getX() + "_y" + point1.getY();

        string += "_G" + isGradient();

        if(secondaryColor != null) {
            string += "___" + CCColorUtils.rgb2hex(secondaryColor);
            if(secondaryColor.getAlpha() != 255)
                string += "_a" + secondaryColor.getAlpha();
            if (point2 != null && !point2.equals(POINT_SECONDARY_DEFAULT))
                string += "_x" + point2.getX() + "_y" + point2.getY();
        }
        return string;
    }

    public static CCColor fromSaveString(String string) {
        CCColor newColor = new CCColor();
        int index = 0;
        for(String part : string.split("___")) {
            int alpha = -1;
            Color color = null;

            float defaultPos = 0;
            if(index != 0) defaultPos = 1;
            CCVector2Float pos = new CCVector2Float(defaultPos, defaultPos);
            for(String str : part.split("_")) {
                switch(str.charAt(0)) {
                    case '#': color = CCColorUtils.hex2rgb(str); break;
                    case 'a': alpha = Integer.parseInt(str.substring(1)); break;
                    case 'x': pos.setX(Float.parseFloat(str.substring(1))); break;
                    case 'y': pos.setY(Float.parseFloat(str.substring(1))); break;
                    case 'G': newColor.isGradient = Boolean.parseBoolean(str.substring(1)); break;
                }

                if(alpha == -1 && color != null)
                    alpha = color.getAlpha();

                if(color != null) {
                    if(index == 0)
                        newColor.primaryColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                    else if(index == 1)
                        newColor.secondaryColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
                }


                if(index == 0) newColor.point1 = pos;
                else if(index == 1) newColor.point2 = pos;
            }
            index++;
        }
        return newColor;
    }

    public void loadFromCCColor(CCColor otherColor) {
        primaryColor = otherColor.primaryColor;
        secondaryColor = otherColor.secondaryColor;
        point1 = otherColor.point1;
        point2 = otherColor.point2;
        isGradient = otherColor.isGradient;
        alertChangeListeners();
    }

    public void setIsGradient(boolean bool) {
        isGradient = bool;
        ensureSecondaryColor();
        alertChangeListeners();
    }

    public void ensureSecondaryColor() {
        if(secondaryColor == null)
            secondaryColor = primaryColor.brighter();
    }

    public boolean isGradient() {
        return isGradient;
    }

    public boolean isValidGradient() {
        return secondaryColor != null;
    }

    @Override
    public String toString() {
        return CCStringUtils.format("CCGradientColor[primaryColor: %c, secondaryColor: %c, point1: %c, point2: %c, isGradient: %c]", primaryColor, secondaryColor, point1, point2, isGradient);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        CCColor other = (CCColor) o;

        return new EqualsBuilder()
                .append(primaryColor.getRGB(), other.primaryColor.getRGB())
                .append(primaryColor.getAlpha(), other.primaryColor.getAlpha())
                .append(secondaryColor.getRGB(), other.secondaryColor.getRGB())
                .append(secondaryColor.getAlpha(), other.secondaryColor.getAlpha())
                .append(isGradient, other.isGradient)
                .append(point1, other.point1)
                .append(point2, other.point2)
                .isEquals();
    }
}
