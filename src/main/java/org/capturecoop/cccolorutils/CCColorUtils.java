package org.capturecoop.cccolorutils;

import org.capturecoop.ccutils.utils.CCStringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CCColorUtils {
    public enum DIRECTION {VERTICAL, HORIZONTAL}

    public static String rgb2hex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color hex2rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf( colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    public static BufferedImage createAlphaBar(Color color, int width, int height, DIRECTION direction) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        int amount = width;
        float step = 255F / width;
        float alpha = 0;

        if(direction == DIRECTION.VERTICAL) {
            amount = height;
            step = 255F / height;
        }

        for(int pos = 0; pos < amount; pos++) {
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)alpha));
            switch(direction) {
                case VERTICAL: g.drawLine(0, pos, width, pos); break;
                case HORIZONTAL: g.drawLine(pos, 0, pos, height); break;
            }
            alpha += step;
        }

        g.dispose();
        return image;
    }

    public static BufferedImage createHSVHueBar(int width, int height, DIRECTION direction) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        float hue = 0F;
        int amount = width;
        float step = 1F / width;

        if(direction == DIRECTION.VERTICAL) {
            amount = height;
            step = 1F / height;
        }

        for(int pos = 0; pos < amount; pos++) {
            g.setColor(new CCHSB(hue, 1F, 1F).toRGB());
            switch(direction) {
                case VERTICAL: g.drawLine(0, pos, width, pos); break;
                case HORIZONTAL: g.drawLine(pos, 0, pos, height); break;
            }
            hue += step;
        }

        g.dispose();
        return image;
    }

    public static BufferedImage createHSVBox(int width, int height, float hue) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        float brightness = 1F;
        float saturation;
        float stepHeight = 1F / height;
        float stepWidth = 1F / width;

        for(int y = 0; y < height; y++) {
            saturation = 0;
            for(int x = 0; x < width; x++) {
                saturation += stepWidth;
                g.setColor(new CCHSB(hue, saturation, brightness).toRGB());
                g.drawLine(x, y, x, y);
            }
            brightness -= stepHeight;
        }

        g.dispose();
        return image;
    }

    public static String toStringColor(Color c) {
        if(c == null)
            return null;
        return CCStringUtils.format("Color[r: %c, g: %c, b: %c, a: %c]", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static Color setColorRed(Color c, int red) {
        return new Color(red, c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public static Color setColorGreen(Color c, int green) {
        return new Color(c.getRed(), green, c.getBlue(), c.getAlpha());
    }

    public static Color setColorBlue(Color c, int blue) {
        return new Color(c.getRed(), c.getGreen(), blue, c.getAlpha());
    }

    public static Color setColorAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static void drawRect(Graphics g, Rectangle rect) {
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    public static void fillRect(Graphics g, Rectangle rect) {
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    public static Color getContrastColor(Color color) {
        double y = (299f * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }
}
