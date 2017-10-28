package mjaroslav.mcmods.mjutils.common.utils;

import java.awt.Color;

/**
 * 
 * @author MJaroslav
 *
 */
public class ColorUtils {
	/**
	 * @return Integer color from hex.
	 */
	public static int getColorInt(String hex) {
		return getColor(hex).getRGB();
	}

	/**
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
	 * @return Red color value from hex.
	 */
	public static int getRed(String hex) {
		return getColor(hex).getRed();
	}

	/**
	 * @return Green color value from hex.
	 */
	public static int getGreen(String hex) {
		return getColor(hex).getGreen();
	}

	/**
	 * @return Blue color value from hex.
	 */
	public static int getBlue(String hex) {
		return getColor(hex).getBlue();
	}

	/**
	 * @return Alpha value from hex.
	 */
	public static int getAlpha(String hex) {
		return getColor(hex).getAlpha();
	}

	/**
	 * @return hex string from color.
	 */
	public static String getHEXString(Color color) {
		String result = "";
		int alpha = 255;
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
