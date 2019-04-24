package mjaroslav.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class UtilsMonitor {

    public static Dimension getSize() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        return kit.getScreenSize();
    }

    public static int getWidth() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize.width;
    }

    public static int getHeight() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        return screenSize.height;
    }

    public static int getCenterHorizontal(int offset) {
        return getWidth() / 2 - offset;
    }

    public static int getCenterVertical(int offset) {
        return getHeight() / 2 - offset;
    }
}
