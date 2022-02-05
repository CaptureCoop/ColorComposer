package org.capturecoop.cccolorutils;

import org.capturecoop.cccolorutils.chooser.CCColorChooser;

import javax.swing.*;
import java.awt.*;

public class MainTest {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new CCColorChooser(new CCColor(Color.GREEN), "Cool chooser", 512, 512, true, null, null);
    }
}
