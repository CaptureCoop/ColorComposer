package org.capturecoop.cccolorutils;

import org.capturecoop.cccolorutils.gui.parts.CCAlphaBar;
import org.capturecoop.cccolorutils.gui.parts.CCHSBHueBar;
import org.capturecoop.cccolorutils.gui.parts.CCHSBPicker;

import javax.swing.*;
import java.awt.*;

public class MainTest {
    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.setLayout(new FlowLayout());
        test.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CCColor color = new CCColor(new Color(200, 100, 120, 200));

        CCHSBPicker picker = new CCHSBPicker(color.getRawColor(), true);
        CCHSBHueBar hueBar = new CCHSBHueBar(color.getRawColor(), CCColorUtils.DIRECTION.VERTICAL, true);
        CCAlphaBar alphaBar = new CCAlphaBar(color.getRawColor(), CCColorUtils.DIRECTION.VERTICAL, true);
        CCAlphaBar alphaBar2 = new CCAlphaBar(color.getRawColor(), CCColorUtils.DIRECTION.HORIZONTAL, true);

        picker.addChangeListener(e -> {
            Color c = picker.getAsColor();
            alphaBar.setBackgroundColor(c);
            alphaBar2.setBackgroundColor(c);

            color.setColor(c, color.getAlpha());
        });

        hueBar.addChangeListener(e -> {
            float hue = hueBar.getHue();
            picker.setHue(hue);
            alphaBar.setBackgroundColor(picker.getAsColor());
            alphaBar2.setBackgroundColor(picker.getAsColor());

            color.setColor(picker.getAsColor());
        });

        alphaBar.addChangeListener(e -> {
            int newAlpha = alphaBar.getAlpha();
            alphaBar2.setAlpha(newAlpha);

            color.setAlpha(newAlpha);
        });

        alphaBar2.addChangeListener(e -> {
            int newAlpha = alphaBar2.getAlpha();
            alphaBar.setAlpha(newAlpha);

            color.setAlpha(newAlpha);
        });

        color.addChangeListener(e -> System.out.println(color));

        picker.setPreferredSize(new Dimension(256, 256));
        hueBar.setPreferredSize(new Dimension(32, 256));
        alphaBar.setPreferredSize(new Dimension(32, 256));
        alphaBar2.setPreferredSize(new Dimension(256, 32));


        test.add(picker);
        test.add(hueBar);
        test.add(alphaBar);
        test.add(alphaBar2);

        test.setSize(512, 512);
        test.setVisible(true);
    }
}
