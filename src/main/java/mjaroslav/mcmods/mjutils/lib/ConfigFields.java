package mjaroslav.mcmods.mjutils.lib;

import mjaroslav.mcmods.mjutils.common.objects.ConfigField;

public class ConfigFields {
	@ConfigField(defaultString = "hehehe", comment = "String value")
	public static String test0;

	@ConfigField(comment = "Float value")
	public static float test1;

	@ConfigField(defaultIntArray = { 4, 3, 2, 1 }, comment = "Int array value")
	public static int[] test2;
}