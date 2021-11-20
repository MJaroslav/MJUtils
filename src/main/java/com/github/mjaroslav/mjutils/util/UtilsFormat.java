package com.github.mjaroslav.mjutils.util;

public class UtilsFormat {
    public static float[] unpackARGBInt(int argb) {
        float a = (float)(argb >> 24 & 255) / 255.0F;
        float r = (float)(argb >> 16 & 255) / 255.0F;
        float g = (float)(argb >> 8 & 255) / 255.0F;
        float b = (float)(argb & 255) / 255.0F;
        return new float[] {a, r, g, b};
    }
}
