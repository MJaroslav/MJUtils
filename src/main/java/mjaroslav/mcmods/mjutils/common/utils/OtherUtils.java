package mjaroslav.mcmods.mjutils.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class OtherUtils {
	public static String string(String str) {
		return StringUtils.isNullOrEmpty(str) ? "" : str;
	}

	public static String nameFormat(ItemStack stack) {
		if (stack == null)
			return "";
		return stack.getDisplayName().replace(" ", "").toLowerCase();
	}

	public static String nameFormat(String name) {
		return name.replace(" ", "").toLowerCase();
	}
}
