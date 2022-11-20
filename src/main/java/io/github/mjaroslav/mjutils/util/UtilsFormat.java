package io.github.mjaroslav.mjutils.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@UtilityClass
public class UtilsFormat {
    public boolean isMaskAnd(int test, int masks) {
        return (test & masks) == masks;
    }

    public boolean isMaskOr(int test, int masks) {
        return (test & masks) != 0;
    }

    public int @NotNull [] unpackColorIntToIntArray(int packedColor) {
        return unpackColorIntToIntArray(packedColor, ColorFormat.ARGB, ColorFormat.ARGB);
    }

    public int @NotNull [] unpackColorIntToIntArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToIntArray(packedColor, inFormat, ColorFormat.ARGB);
    }

    public int @NotNull [] unpackColorIntToIntArray(int packedColor, @NotNull ColorFormat inFormat,
                                                    @NotNull ColorFormat outFormat) {
        val result = new int[4];
        if (inFormat.hasAlpha)
            result[outFormat.aArrayPos] = (packedColor >> inFormat.aBytePos) & 0xFF;
        else
            result[outFormat.aArrayPos] = 0;
        result[outFormat.rArrayPos] = (packedColor >> inFormat.rBytePos) & 0xFF;
        result[outFormat.gArrayPos] = (packedColor >> inFormat.gBytePos) & 0xFF;
        result[outFormat.bArrayPos] = (packedColor >> inFormat.bBytePos) & 0xFF;
        return result;
    }

    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor) {
        return unpackColorIntToDoubleArray(packedColor, ColorFormat.ARGB, ColorFormat.ARGB);
    }

    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToDoubleArray(packedColor, inFormat, ColorFormat.ARGB);
    }

    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor, @NotNull ColorFormat inFormat,
                                                          @NotNull ColorFormat outFormat) {
        return Arrays.stream(unpackColorIntToIntArray(packedColor, inFormat, outFormat))
            .mapToDouble(i -> i / 255D).toArray();
    }

    public float @NotNull [] unpackColorIntToFloatArray(int packedColor) {
        return unpackColorIntToFloatArray(packedColor, ColorFormat.ARGB, ColorFormat.ARGB);
    }

    public float @NotNull [] unpackColorIntToFloatArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToFloatArray(packedColor, inFormat, ColorFormat.ARGB);
    }

    public float @NotNull [] unpackColorIntToFloatArray(int packedColor, @NotNull ColorFormat inFormat,
                                                        @NotNull ColorFormat outFormat) {
        val arrayInt = unpackColorIntToIntArray(packedColor, inFormat, outFormat);
        val result = new float[arrayInt.length];
        for (var i = 0; i < arrayInt.length; i++)
            result[i] = arrayInt[i] / 255F;
        return result;
    }

    public int packToColorInt(float a, float r, float g, float b) {
        return packToColorInt((double) a, r, g, b, ColorFormat.ARGB);
    }

    public int packToColorInt(float a, float r, float g, float b, @NotNull ColorFormat outFormat) {
        return packToColorInt((double) a, r, g, b, outFormat);
    }

    public int packToColorInt(double a, double r, double g, double b) {
        return packToColorInt(a, r, g, b, ColorFormat.ARGB);
    }

    public int packToColorInt(double a, double r, double g, double b, @NotNull ColorFormat outFormat) {
        var aInt = (((int) (a * 0xFF)) & 0xFF) << outFormat.aBytePos;
        var rInt = (((int) (r * 0xFF)) & 0xFF) << outFormat.rBytePos;
        var gInt = (((int) (g * 0xFF)) & 0xFF) << outFormat.gBytePos;
        var bInt = (((int) (b * 0xFF)) & 0xFF) << outFormat.bBytePos;
        return aInt + rInt + gInt + bInt;
    }

    public int packToColorInt(int a, int r, int g, int b) {
        return packToColorInt(a, r, g, b, ColorFormat.ARGB);
    }

    public int packToColorInt(int a, int r, int g, int b, @NotNull ColorFormat outFormat) {
        a = (a & 0xFF) << outFormat.aBytePos;
        r = (r & 0xFF) << outFormat.rBytePos;
        g = (g & 0xFF) << outFormat.gBytePos;
        b = (b & 0xFF) << outFormat.bBytePos;
        return a + r + g + b;
    }

    @RequiredArgsConstructor
    public enum ColorFormat {
        RGBA(3, 0, 1, 2, 0, 24, 16, 8, true),
        ARGB(0, 1, 2, 3, 24, 16, 8, 0, true),
        RGB(3, 0, 1, 2, 24, 16, 8, 0, false),
        BGR(3, 2, 1, 0, 24, 0, 8, 16, false),
        BGRA(3, 2, 1, 0, 0, 8, 16, 24, true),
        ABGR(0, 3, 2, 1, 24, 0, 8, 16, true);

        public final int aArrayPos;
        public final int rArrayPos;
        public final int gArrayPos;
        public final int bArrayPos;

        public final int aBytePos;
        public final int rBytePos;
        public final int gBytePos;
        public final int bBytePos;

        public final boolean hasAlpha;
    }
}
