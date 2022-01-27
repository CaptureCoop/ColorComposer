package org.capturecoop.cccolorutils;

import org.capturecoop.cccolorutils.gui.CCColorChooser;

import java.awt.*;

public class MainTest {
    public static void main(String[] args) {
        new CCColorChooser(new CCColor(Color.GREEN), "Cool chooser", 512, 512, true, null, null);
    }
}
