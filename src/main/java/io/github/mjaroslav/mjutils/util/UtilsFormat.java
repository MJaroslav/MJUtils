package io.github.mjaroslav.mjutils.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat.*;

/**
 * Utilities for transferring between different simple formats such as packed int color.
 * <p>
 * Also contain some methods for checking bits, bit mask for example.
 */
@UtilityClass
public class UtilsFormat {
    /**
     * Test int with bit mask for full match of bits.
     *
     * @param test int for test.
     * @param mask bit mask.
     * @return true if and only if test contains all bits from mask.
     */
    public boolean isMaskAnd(int test, int mask) {
        return (test & mask) == mask;
    }

    /**
     * Test int with bit mask for match of any bit.
     *
     * @param test int for test.
     * @param mask bit mask.
     * @return true if test and mask has at lest one shared bit.
     */
    public boolean isMaskOr(int test, int mask) {
        return (test & mask) != 0;
    }

    /**
     * Unpack int color with {@link ColorFormat#ARGB ARGB} to int array with same format.
     *
     * @param packedColor color for unpacking.
     * @return int array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..255 range).
     */
    public int @NotNull [] unpackColorIntToIntArray(int packedColor) {
        return unpackColorIntToIntArray(packedColor, ARGB, ARGB);
    }

    /**
     * Unpack int color with inFormat to int array with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @return int array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..255 range).
     */
    public int @NotNull [] unpackColorIntToIntArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToIntArray(packedColor, inFormat, ARGB);
    }

    /**
     * Unpack int color with inFormat to int array with outFormat.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @param outFormat   format for result unpacked color array.
     * @return int array of unpacked color in outFormat (all channels in 0..255 range).
     */
    public int @NotNull [] unpackColorIntToIntArray(int packedColor, @NotNull ColorFormat inFormat,
                                                    @NotNull ColorFormat outFormat) {
        val result = new int[4];
        if (inFormat.hasAlpha) result[outFormat.aArrayPos] = (packedColor >> inFormat.aBytePos) & 0xFF;
        else result[outFormat.aArrayPos] = 0;
        result[outFormat.rArrayPos] = (packedColor >> inFormat.rBytePos) & 0xFF;
        result[outFormat.gArrayPos] = (packedColor >> inFormat.gBytePos) & 0xFF;
        result[outFormat.bArrayPos] = (packedColor >> inFormat.bBytePos) & 0xFF;
        return result;
    }

    /**
     * Unpack int color with {@link ColorFormat#ARGB ARGB} to double array with same format.
     *
     * @param packedColor color for unpacking.
     * @return double array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..1 range).
     */
    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor) {
        return unpackColorIntToDoubleArray(packedColor, ARGB, ARGB);
    }

    /**
     * Unpack int color with inFormat to double array with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @return double array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..1 range).
     */
    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToDoubleArray(packedColor, inFormat, ARGB);
    }

    /**
     * Unpack int color with inFormat to double array with outFormat.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @param outFormat   format for result unpacked color array.
     * @return double array of unpacked color in outFormat (all channels in 0..1 range).
     */
    public double @NotNull [] unpackColorIntToDoubleArray(int packedColor, @NotNull ColorFormat inFormat,
                                                          @NotNull ColorFormat outFormat) {
        return Arrays.stream(unpackColorIntToIntArray(packedColor, inFormat, outFormat))
            .mapToDouble(i -> i / 255D).toArray();
    }

    /**
     * Unpack int color with {@link ColorFormat#ARGB ARGB} to float array with same format.
     *
     * @param packedColor color for unpacking.
     * @return float array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..1 range).
     */
    public float @NotNull [] unpackColorIntToFloatArray(int packedColor) {
        return unpackColorIntToFloatArray(packedColor, ARGB, ARGB);
    }

    /**
     * Unpack int color with inFormat to float array with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @return float array of unpacked color in {@link ColorFormat#ARGB ARGB} (all channels in 0..1 range).
     */
    public float @NotNull [] unpackColorIntToFloatArray(int packedColor, @NotNull ColorFormat inFormat) {
        return unpackColorIntToFloatArray(packedColor, inFormat, ARGB);
    }

    /**
     * Unpack int color with inFormat to float array with outFormat.
     *
     * @param packedColor color for unpacking.
     * @param inFormat    format of int packed color.
     * @param outFormat   format for result unpacked color array.
     * @return float array of unpacked color in outFormat (all channels in 0..1 range).
     */
    public float @NotNull [] unpackColorIntToFloatArray(int packedColor, @NotNull ColorFormat inFormat,
                                                        @NotNull ColorFormat outFormat) {
        val arrayInt = unpackColorIntToIntArray(packedColor, inFormat, outFormat);
        val result = new float[arrayInt.length];
        for (var i = 0; i < arrayInt.length; i++)
            result[i] = arrayInt[i] / 255F;
        return result;
    }

    /**
     * Pack float color channels (in 0..1 range) to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param a alpha channel. 0 if alpha not supported by outFormat.
     * @param r red color channel.
     * @param g green color channel.
     * @param b blue color channel.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(float, float, float, float, ColorFormat)
     */
    public int packToColorInt(float a, float r, float g, float b) {
        return packToColorInt((double) a, r, g, b, ARGB);
    }

    /**
     * Pack float color channels (in 0..1 range) to color int.
     *
     * @param a         alpha channel. 0 if alpha not supported by outFormat.
     * @param r         red color channel.
     * @param g         green color channel.
     * @param b         blue color channel.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(float a, float r, float g, float b, @NotNull ColorFormat outFormat) {
        if (!outFormat.hasAlpha) a = 0;
        val aInt = ((int) (a * 0xFF) & 0xFF) << outFormat.aBytePos;
        val rInt = ((int) (r * 0xFF) & 0xFF) << outFormat.rBytePos;
        val gInt = ((int) (g * 0xFF) & 0xFF) << outFormat.gBytePos;
        val bInt = ((int) (b * 0xFF) & 0xFF) << outFormat.bBytePos;
        return aInt + rInt + gInt + bInt;
    }

    /**
     * Pack double color channels (in 0..1 range) to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param a alpha channel. 0 if alpha not supported by outFormat.
     * @param r red color channel.
     * @param g green color channel.
     * @param b blue color channel.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(double, double, double, double, ColorFormat)
     */
    public int packToColorInt(double a, double r, double g, double b) {
        return packToColorInt(a, r, g, b, ARGB);
    }

    /**
     * Pack double color channels (in 0..1 range) to color int.
     *
     * @param a         alpha channel. 0 if alpha not supported by outFormat.
     * @param r         red color channel.
     * @param g         green color channel.
     * @param b         blue color channel.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(double a, double r, double g, double b, @NotNull ColorFormat outFormat) {
        if (!outFormat.hasAlpha) a = 0;
        val aInt = ((int) (a * 0xFF) & 0xFF) << outFormat.aBytePos;
        val rInt = ((int) (r * 0xFF) & 0xFF) << outFormat.rBytePos;
        val gInt = ((int) (g * 0xFF) & 0xFF) << outFormat.gBytePos;
        val bInt = ((int) (b * 0xFF) & 0xFF) << outFormat.bBytePos;
        return aInt + rInt + gInt + bInt;
    }

    /**
     * Pack int (in 0..255 range) color channels to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param a alpha channel. 0 if alpha not supported by outFormat.
     * @param r red color channel.
     * @param g green color channel.
     * @param b blue color channel.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int, int, int, int, ColorFormat)
     */
    public int packToColorInt(int a, int r, int g, int b) {
        return packToColorInt(a, r, g, b, ARGB);
    }

    /**
     * Pack int (in 0..255 range) color channels to color int.
     *
     * @param a         alpha channel. 0 if alpha not supported by outFormat.
     * @param r         red color channel.
     * @param g         green color channel.
     * @param b         blue color channel.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(int a, int r, int g, int b, @NotNull ColorFormat outFormat) {
        if (!outFormat.hasAlpha) a = 0;
        a = (a & 0xFF) << outFormat.aBytePos;
        r = (r & 0xFF) << outFormat.rBytePos;
        g = (g & 0xFF) << outFormat.gBytePos;
        b = (b & 0xFF) << outFormat.bBytePos;
        return a + r + g + b;
    }

    /**
     * Pack int (in 0..255 range) color array to color int.
     *
     * @param unpacked  array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat  array color format.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(int @NotNull [] unpacked, @NotNull ColorFormat inFormat, @NotNull ColorFormat outFormat) {
        if (inFormat.hasAlpha ? unpacked.length != 4 : (unpacked.length != 4 && unpacked.length != 3))
            throw new IllegalArgumentException("unpacked array must be with size 3 or 4 if inFormat has alpha channel");
        val a = (inFormat.hasAlpha ? (unpacked[inFormat.aArrayPos] & 0xFF) : 0) << outFormat.aBytePos;
        val r = (unpacked[inFormat.rArrayPos] & 0xFF) << outFormat.rBytePos;
        val g = (unpacked[inFormat.gArrayPos] & 0xFF) << outFormat.gBytePos;
        val b = (unpacked[inFormat.bArrayPos] & 0xFF) << outFormat.bBytePos;
        return a + r + g + b;
    }

    /**
     * Pack int (in 0..255 range) color array to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat array color format.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(int @NotNull [] unpacked, @NotNull ColorFormat inFormat) {
        return packToColorInt(unpacked, inFormat, ARGB);
    }

    /**
     * Pack int (in 0..255 range) color array with {@link ColorFormat#ARGB ARGB} format to color int with same format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(int @NotNull [] unpacked) {
        return packToColorInt(unpacked, ARGB, ARGB);
    }

    /**
     * Pack float (in 0..1 range) color array to color int.
     *
     * @param unpacked  array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat  array color format.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(float @NotNull [] unpacked, @NotNull ColorFormat inFormat, @NotNull ColorFormat outFormat) {
        if (inFormat.hasAlpha ? unpacked.length != 4 : (unpacked.length != 4 && unpacked.length != 3))
            throw new IllegalArgumentException("unpacked array must be with size 3 or 4 if inFormat has alpha channel");
        val a = (inFormat.hasAlpha ? ((int) (unpacked[inFormat.aArrayPos] * 255F) & 0xFF) : 0) << outFormat.aBytePos;
        val r = ((int) (unpacked[inFormat.rArrayPos] * 255F) & 0xFF) << outFormat.rBytePos;
        val g = ((int) (unpacked[inFormat.gArrayPos] * 255F) & 0xFF) << outFormat.gBytePos;
        val b = ((int) (unpacked[inFormat.bArrayPos] * 255F) & 0xFF) << outFormat.bBytePos;
        return a + r + g + b;
    }

    /**
     * Pack float (in 0..255 range) color array to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat array color format.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(float @NotNull [] unpacked, @NotNull ColorFormat inFormat) {
        return packToColorInt(unpacked, inFormat, ARGB);
    }

    /**
     * Pack float (in 0..255 range) color array with {@link ColorFormat#ARGB ARGB} format to color int with same format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(float @NotNull [] unpacked) {
        return packToColorInt(unpacked, ARGB, ARGB);
    }

    /**
     * Pack double (in 0..1 range) color array to color int.
     *
     * @param unpacked  array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat  array color format.
     * @param outFormat out color format.
     * @return packed color integer.
     */
    public int packToColorInt(double @NotNull [] unpacked, @NotNull ColorFormat inFormat, @NotNull ColorFormat outFormat) {
        if (inFormat.hasAlpha ? unpacked.length != 4 : (unpacked.length != 4 && unpacked.length != 3))
            throw new IllegalArgumentException("unpacked array must be with size 3 or 4 if inFormat has alpha channel");
        val a = (inFormat.hasAlpha ? ((int) (unpacked[inFormat.aArrayPos] * 255D) & 0xFF) : 0) << outFormat.aBytePos;
        val r = ((int) (unpacked[inFormat.rArrayPos] * 255D) & 0xFF) << outFormat.rBytePos;
        val g = ((int) (unpacked[inFormat.gArrayPos] * 255D) & 0xFF) << outFormat.gBytePos;
        val b = ((int) (unpacked[inFormat.bArrayPos] * 255D) & 0xFF) << outFormat.bBytePos;
        return a + r + g + b;
    }

    /**
     * Pack double (in 0..1 range) color array to color int with {@link ColorFormat#ARGB ARGB} format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @param inFormat array color format.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(double @NotNull [] unpacked, @NotNull ColorFormat inFormat) {
        return packToColorInt(unpacked, inFormat, ARGB);
    }

    /**
     * Pack double (in 0..1 range) color array with {@link ColorFormat#ARGB ARGB} format to color int with same format.
     *
     * @param unpacked array with color channels, must contain 4 elements for alpha formats else can be 3.
     * @return packed color integer.
     * @see UtilsFormat#packToColorInt(int[], ColorFormat, ColorFormat)
     */
    public int packToColorInt(double @NotNull [] unpacked) {
        return packToColorInt(unpacked, ARGB, ARGB);
    }

    /**
     * Formats of packed color, contains HEX bit positions and array index for each color chanel.
     */
    @RequiredArgsConstructor
    public enum ColorFormat {
        /**
         * #RRGGBBAA or [r, g, b, a].
         */
        RGBA(3, 0, 1, 2, 0, 24, 16, 8, true),
        /**
         * #AARRGGBB or [a, r, g, b].
         */
        ARGB(0, 1, 2, 3, 24, 16, 8, 0, true),
        /**
         * #RRGGBB (#00RRGGBB) or [r, g, b, 0].
         */
        RGB(3, 0, 1, 2, 24, 16, 8, 0, false),
        /**
         * #BBGGRR (#00BBGGRR) or [b, g, r, 0].
         */
        BGR(3, 2, 1, 0, 24, 0, 8, 16, false),
        /**
         * #BBGGRRAA or [b, g, r, a].
         */
        BGRA(3, 2, 1, 0, 0, 8, 16, 24, true),
        /**
         * #AABBGGRR or [a, b, g, r].
         */
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
