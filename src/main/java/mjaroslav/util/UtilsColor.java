package mjaroslav.util;

import java.awt.Color;

/**
 * Color util (hex and int values).
 *
 * @author MJaroslav
 */
public class UtilsColor {
    /**
     * Change hex color value to int.
     *
     * @param hex
     *            - hex color.
     * @return Integer color from hex.
     */
    public static int getColorInt(String hex) {
        return getColor(hex).getRGB();
    }

    /**
     * Change hex color value to color instance.
     *
     * @param hex
     *            - hex color.
     * @return Color from hex (AARRGGBB or RRGGBB).
     */
    public static Color getColor(String hex) {
        int colorInt;
        boolean hasAlpha = false;
        int alpha = 255;
        if (hex.length() == 8)
            hasAlpha = true;
        try {
            if (hasAlpha) {
                colorInt = Integer.parseInt(hex.substring(2, 8), 16);
                alpha = Integer.parseInt(hex.substring(0, 2), 16);
            } else
                colorInt = Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            colorInt = 0x000000;
        }
        Color temp = new Color(colorInt);
        return hasAlpha ? new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), alpha) : temp;
    }

    /**
     * Get red int value from hex color.
     *
     * @param hex
     *            - hex color.
     * @return Red from hex.
     */
    public static int getRed(String hex) {
        return getColor(hex).getRed();
    }

    /**
     * Get green int value from hex color.
     *
     * @param hex
     *            - hex color.
     * @return Green from hex.
     */
    public static int getGreen(String hex) {
        return getColor(hex).getGreen();
    }

    /**
     * Get blue int value from hex color.
     *
     * @param hex
     *            - hex color.
     * @return Blue from hex.
     */
    public static int getBlue(String hex) {
        return getColor(hex).getBlue();
    }

    /**
     * Get alpha int value from hex color.
     *
     * @param hex
     *            - hex color.
     * @return Alpha from hex.
     */
    public static int getAlpha(String hex) {
        return getColor(hex).getAlpha();
    }

    /**
     * Get hex string from color
     *
     * @param color
     *            - color.
     * @return Hex string.
     */
    public static String getHEXString(Color color) {
        String result = "";
        int alpha;
        if (color.getAlpha() < 255) {
            alpha = color.getAlpha();
            result += Integer.toHexString(alpha);
        }
        int red = color.getRed();
        result += Integer.toHexString(red);
        int green = color.getGreen();
        result += Integer.toHexString(green);
        int blue = color.getBlue();
        result += Integer.toHexString(blue);
        return result;
    }
}
