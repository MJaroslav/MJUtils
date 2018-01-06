package mjaroslav.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class MonitorUtils {

	public static Dimension getSize() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		return kit.getScreenSize();
	}

	public static int getWidth() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		return screenSize.width;
	}

	public static int getHeigt() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		return screenSize.height;
	}

	public static int getCenterHorizontal() {
		return getWidth() / 2;
	}

	public static int getCenterVertical() {
		return getHeigt() / 2;
	}

	public static Point getCenter() {
		return new Point(getCenterHorizontal(), getCenterVertical());
	}

	public static Point getCenter(int offsetH, int offsetV) {
		return new Point(getCenterHorizontal() - offsetH, getCenterVertical() - offsetV);
	}

	public static Point getCenter(Dimension object) {
		return new Point(getCenterHorizontal() - object.height, getCenterVertical() - object.width);
	}

	public static Point getCenter(Dimension object, int offsetH, int offsetV) {
		return new Point(getCenterHorizontal() - object.height - offsetH, getCenterVertical() - object.width - offsetV);
	}

	public static Point getRightBottom(int offsetH, int offsetV) {
		return new Point(getWidth() - offsetH, getHeigt() - offsetV);
	}

	public static Point getLeftTop(int offsetH, int offsetV) {
		return new Point(offsetH, offsetV);
	}

	public static Point getLeftBottom(int offsetH, int offsetV) {
		return new Point(offsetH, getHeigt() - offsetV);
	}

	public static Point getRightTop(int offsetH, int offsetV) {
		return new Point(getWidth() - offsetH, offsetV);
	}

	public static Point getRightBottom(Dimension object) {
		return new Point(getWidth() - object.width, getHeigt() - object.height);
	}

	public static Point getLeftTop(Dimension object) {
		return new Point(object.width, object.height);
	}

	public static Point getLeftBottom(Dimension object) {
		return new Point(object.width, getHeigt() - object.height);
	}

	public static Point getRightTop(Dimension object) {
		return new Point(getWidth() - object.width, object.height);
	}

	public static Point getRightBottom(Dimension object, int offsetH, int offsetV) {
		return new Point(getWidth() - object.width - offsetH, getHeigt() - object.height - offsetV);
	}

	public static Point getLeftTop(Dimension object, int offsetH, int offsetV) {
		return new Point(object.width + offsetH, object.height + offsetV);
	}

	public static Point getLeftBottom(Dimension object, int offsetH, int offsetV) {
		return new Point(object.width + offsetH, getHeigt() - object.height - offsetV);
	}

	public static Point getRightTop(Dimension object, int offsetH, int offsetV) {
		return new Point(getWidth() - object.width - offsetH, object.height + offsetV);
	}
}
