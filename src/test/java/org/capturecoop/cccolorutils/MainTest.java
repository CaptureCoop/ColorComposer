package org.capturecoop.cccolorutils;

import org.capturecoop.cccolorutils.gui.CCAlphaBar;
import org.capturecoop.cccolorutils.gui.CCHSBHueBar;
import org.capturecoop.cccolorutils.gui.CCHSBPicker;

import javax.swing.*;
import java.awt.*;

public class MainTest {
    public static void main(String[] args) {
        JFrame test = new JFrame();
        test.setLayout(new FlowLayout());
        test.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        CCColor color = new CCColor(Color.RED);

        CCHSBPicker picker = new CCHSBPicker(color, false);
        picker.setPreferredSize(new Dimension(256, 256));

        CCHSBHueBar hueBar = new CCHSBHueBar(color, CCColorUtils.DIRECTION.VERTICAL, false);
        hueBar.setPreferredSize(new Dimension(32, 256));

        CCAlphaBar alphaBar = new CCAlphaBar(color, CCColorUtils.DIRECTION.VERTICAL, false);
        alphaBar.setPreferredSize(new Dimension(32, 256));

        CCAlphaBar alphaBar2 = new CCAlphaBar(color, CCColorUtils.DIRECTION.HORIZONTAL, false);
        alphaBar2.setPreferredSize(new Dimension(256, 32));

        test.add(picker);
        test.add(hueBar);
        test.add(alphaBar);
        test.add(alphaBar2);

        test.setSize(512, 512);
        test.setVisible(true);
    }
}
