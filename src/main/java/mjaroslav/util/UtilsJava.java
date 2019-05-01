package mjaroslav.util;

import net.minecraft.item.ItemStack;

public class UtilsJava {
    public static boolean stringsNotNullAndEquals(String a, String b) {
        return a != null && a.equals(b);
    }

    public static boolean stringIsNotEmpty(String input) {
        return input != null && input.length() > 0;
    }

    public static boolean stringIsEmpty(String input) {
        return !stringIsNotEmpty(input);
    }

    /**
     * Always return string.
     *
     * @param input - String object.
     * @return Empty string if null.
     */
    public static String string(String input) {
        return stringIsEmpty(input) ? "" : input;
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

    public static float[] toFloatArray(double[] input) {
        if (input != null) {
            float[] result = new float[input.length];
            for (int id = 0; id < input.length; id++)
                result[id] = (float) input[id];
            return result;
        }
        return new float[]{};
    }

    public static double[] toDoubleArray(float[] input) {
        if (input != null) {
            double[] result = new double[input.length];
            for (int id = 0; id < input.length; id++)
                result[id] = input[id];
            return result;
        }
        return new double[]{};
    }
}
