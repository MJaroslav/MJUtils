package mjaroslav.mcmods.mjutils.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;

public class OtherUtils {
  /**
   * Always return string.
   *
   * @param str - String object.
   * @return Empty string if null.
   */
  public static String string(String str) {
    return StringUtils.isNullOrEmpty(str) ? "" : str;
  }

  /**
   * Format display name from stack.
   *
   * @param stack - ItemStack for format.
   * @return - Display name in lower case without spaces.
   */
  public static String nameFormat(ItemStack stack) {
    return stack == null ? "" : stack.getDisplayName().replace(" ", "").toLowerCase();
  }

  /**
   * Format string, usually display name of ItemStack.
   *
   * @param name - String for format.
   * @return - String in lower case without spaces.
   */
  public static String nameFormat(String name) {
    return name.replace(" ", "").toLowerCase();
  }
}
