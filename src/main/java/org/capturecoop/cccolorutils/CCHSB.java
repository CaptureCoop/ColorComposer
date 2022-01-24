package org.capturecoop.cccolorutils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.capturecoop.ccutils.utils.CCStringUtils;

import java.awt.*;

public class CCHSB {
    private float hue;
    private float saturation;
    private float brightness;
    private int alpha;

    public CCHSB(float hue, float saturation, float brightness) {
        load(hue, saturation, brightness, 255);
    }

    public CCHSB(float hue, float saturation, float brightness, int alpha) {
        load(hue, saturation, brightness, alpha);
    }

    public CCHSB(Color color) {
        float[] values = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), values);
        load(values[0], values[1], values[2], color.getAlpha());
    }

    private void load(float hue, float saturation, float brightness, int alpha) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.alpha = alpha;
    }

    public Color toRGB() {
        Color tempColor = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), alpha);
    }

    public float getHue() {
        return hue;
    }

    public float getSaturation() {
        return saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public int getAlpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return CCStringUtils.format("CCHSB[hue: %c, saturation: %c, brightness: %c, alpha: %c]", hue, saturation, brightness, alpha);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || getClass() != o.getClass())
            return false;

        CCHSB other = (CCHSB) o;

        return new EqualsBuilder()
                .append(hue, other.hue)
                .append(saturation, other.saturation)
                .append(brightness, other.brightness)
                .append(alpha, other.alpha)
                .isEquals();
    }
}
