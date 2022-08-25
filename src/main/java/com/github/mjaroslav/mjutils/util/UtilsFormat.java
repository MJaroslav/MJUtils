package com.github.mjaroslav.mjutils.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@UtilityClass
public class UtilsFormat {
    public int @NotNull [] unpackColorIntToIntArray(int packedColor, @NotNull ColorFormat inFormat,
                                                 @NotNull ColorFormat outFormat) {
        val result = new int[4];
        result[outFormat.aArrayPos] = packedColor >> inFormat.aBytePos & 255;
        result[outFormat.rArrayPos] = packedColor >> inFormat.rBytePos & 255;
        result[outFormat.gArrayPos] = packedColor >> inFormat.gBytePos & 255;
        result[outFormat.bArrayPos] = packedColor >> inFormat.bBytePos & 255;
        return result;
    }

    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor, @NotNull ColorFormat inFormat,
                                                          @NotNull ColorFormat outFormat) {
        return Arrays.stream(unpackColorIntToIntArray(packedColor, inFormat, outFormat))
                .mapToDouble(i -> i / 255D).toArray();
    }

    public float @NotNull [] unpackColorIntToFloatArray(int packedColor, @NotNull ColorFormat inFormat,
                                                        @NotNull ColorFormat outFormat) {
        val arrayInt = unpackColorIntToIntArray(packedColor, inFormat, outFormat);
        return new float[]{arrayInt[0] / 255F, arrayInt[1] / 255F, arrayInt[2] / 255F, arrayInt[3] / 255F};
    }

    public int packToColorInt(float a, float r, float g, float b, @NotNull ColorFormat outFormat) {
        return packToColorInt((double) a, r, g, b, outFormat);
    }

    public int packToColorInt(double a, double r, double g, double b, @NotNull ColorFormat outFormat) {
        var aInt = (int) (a * 255) & 255 << outFormat.aBytePos;
        var rInt = (int) (r * 255) & 255 << outFormat.rBytePos;
        var gInt = (int) (g * 255) & 255 << outFormat.gBytePos;
        var bInt = (int) (b * 255) & 255 << outFormat.bBytePos;
        return aInt + rInt + gInt + bInt;
    }

    public int packToColorInt(int a, int r, int g, int b, @NotNull ColorFormat outFormat) {
        a = a & 255 << outFormat.aBytePos;
        r = r & 255 << outFormat.rBytePos;
        g = g & 255 << outFormat.gBytePos;
        b = b & 255 << outFormat.bBytePos;
        return a + r + g + b;
    }

    @RequiredArgsConstructor
    public enum ColorFormat {
        RGBA(3, 0, 1, 2, 0, 24, 16, 8),
        ARGB(0, 1, 2, 3, 24, 16, 8, 0),
        RGB(3, 0, 1, 2, 24, 16, 8, 0),
        BGR(3, 2, 1, 0, 24, 0, 8, 16),
        BGRA(3, 2, 1, 0, 0, 8, 16, 24),
        ABGR(0, 3, 2, 1, 24, 0, 8, 16);

        public final int aArrayPos;
        public final int rArrayPos;
        public final int gArrayPos;
        public final int bArrayPos;

        public final int aBytePos;
        public final int rBytePos;
        public final int gBytePos;
        public final int bBytePos;
    }
}
