package io.github.mjaroslav.mjutils.util;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import static io.github.mjaroslav.mjutils.util.UtilsFormat.*;
import static io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat.*;

public class TestUtilsFormat {
    private static final int A = 0xAA;
    private static final int R = 0xBB;
    private static final int G = 0xCC;
    private static final int B = 0xDD;
    private static final int MASK = 0b1010;

    private void unpackColorIntShared(int packed, @NotNull ColorFormat format) {
        val actualInt = unpackColorIntToIntArray(packed, format);
        val expectedInt = new int[]{format.hasAlpha ? A : 0, R, G, B};
        Assert.assertArrayEquals("int", expectedInt, actualInt);
        val actualFloat = unpackColorIntToFloatArray(packed, format);
        val expectedFloat = new float[]{(format.hasAlpha ? A : 0) / 255F, R / 255F, G / 255F, B / 255F};
        Assert.assertArrayEquals("float", expectedFloat, actualFloat, 0);
        val actualDouble = unpackColorIntToDoubleArray(packed, format);
        val expectedDouble = new double[]{(format.hasAlpha ? A : 0) / 255D, R / 255D, G / 255D, B / 255D};
        Assert.assertArrayEquals("double", expectedDouble, actualDouble, 0);
    }

    private void packToColorIntSharedArray(int expected, int i0, int i1, int i2, int i3, @NotNull ColorFormat format) {
        var actual = packToColorInt(new int[]{i0, i1, i2, i3}, format);
        Assert.assertEquals("int array", expected, actual);
        actual = packToColorInt(new float[]{i0 / 255F, i1 / 255F, i2 / 255F, i3 / 255F}, format);
        Assert.assertEquals("float array", expected, actual);
        actual = packToColorInt(new double[]{i0 / 255D, i1 / 255D, i2 / 255D, i3 / 255D}, format);
        Assert.assertEquals("double array", expected, actual);
    }

    private void packToColorIntShared(int expected, @NotNull ColorFormat format) {
        var actual = packToColorInt(A, R, G, B, format);
        Assert.assertEquals("int", expected, actual);
        actual = packToColorInt(A / 255F, R / 255F, G / 255F, B / 255F, format);
        Assert.assertEquals("float", expected, actual);
        actual = packToColorInt(A / 255D, R / 255D, G / 255D, B / 255D, format);
        Assert.assertEquals("double", expected, actual);
    }

    @Test
    public void test$packToColorInt$BGRA() {
        val expected = (B << 24) + (G << 16) + (R << 8) + A; // #BBGGRRAA
        packToColorIntShared(expected, BGRA);
    }

    @Test
    public void test$packToColorInt$BGR() {
        val expected = (B << 16) + (G << 8) + R; // #00BBGGRR
        packToColorIntShared(expected, BGR);
    }

    @Test
    public void test$packToColorInt$RGBA() {
        val expected = (R << 24) + (G << 16) + (B << 8) + A; // #RRGGBB00
        packToColorIntShared(expected, RGBA);
    }

    @Test
    public void test$packToColorInt$RGB() {
        val expected = (R << 16) + (G << 8) + B; // #00RRGGBB
        packToColorIntShared(expected, RGB);
    }

    @Test
    public void test$packToColorInt$ARGB() {
        val expected = (A << 24) + (R << 16) + (G << 8) + B; // #AARRGGBB
        packToColorIntShared(expected, ARGB);
    }

    @Test
    public void test$packToColorInt$ABGR() {
        val expected = (A << 24) + (B << 16) + (G << 8) + R; // #AABBGGRR
        packToColorIntShared(expected, ABGR);
    }

    @Test
    public void test$packToColorInt$BGRA$array() {
        val expected = 0xAABBCCDD; // #AARRGGBB
        packToColorIntSharedArray(expected, B, G, R, A, BGRA);
    }

    @Test
    public void test$packToColorInt$BGR$array() {
        val expected = 0xBBCCDD; // #00RRGGBB
        packToColorIntSharedArray(expected, B, G, R, 0, BGR);
    }

    @Test
    public void test$packToColorInt$RGBA$array() {
        val expected = 0xAABBCCDD; // #AARRGGBB
        packToColorIntSharedArray(expected, R, G, B, A, RGBA);
    }

    @Test
    public void test$packToColorInt$RGB$array() {
        val expected = 0x00BBCCDD; // #00RRGGBB
        packToColorIntSharedArray(expected, R, G, B, 0, RGB);
    }

    @Test
    public void test$packToColorInt$ARGB$array() {
        val expected = 0xAABBCCDD; // #AARRGGBB
        packToColorIntSharedArray(expected, A, R, G, B, ARGB);
    }

    @Test
    public void test$packToColorInt$ABGR$array() {
        val expected = 0xAABBCCDD; // #AARRGGBB
        packToColorIntSharedArray(expected, A, B, G, R, ABGR);
    }

    @Test
    public void test$unpackColorInt$BGRA() {
        val packed = (B << 24) + (G << 16) + (R << 8) + A; // #BBGGRRAA
        unpackColorIntShared(packed, BGRA);
    }

    @Test
    public void test$unpackColorInt$BGR() {
        val packed = (B << 16) + (G << 8) + R; // #00BBGGRR
        unpackColorIntShared(packed, BGR);
    }

    @Test
    public void test$unpackColorInt$RGBA() {
        val packed = (R << 24) + (G << 16) + (B << 8) + A; // #RRGGBBAA
        unpackColorIntShared(packed, RGBA);
    }

    @Test
    public void test$unpackColorInt$RGB() {
        val packed = (R << 16) + (G << 8) + B; // #00RRGGBB
        unpackColorIntShared(packed, RGB);
    }

    @Test
    public void test$unpackColorInt$ARGB() {
        val packed = (A << 24) + (R << 16) + (G << 8) + B; // #AARRGGBB
        unpackColorIntShared(packed, ARGB);
    }


    @Test
    public void test$unpackColorInt$ABGR() {
        val packed = (A << 24) + (B << 16) + (G << 8) + R; // #AABBGGRR
        unpackColorIntShared(packed, ABGR);
    }

    @Test
    public void test$isMaskAnd() {
        Assert.assertFalse("Not match", isMaskAnd(0b0000, MASK));
        Assert.assertTrue("Match", isMaskAnd(0b1010, MASK));
        Assert.assertFalse("Match not all bits", isMaskAnd(0b1000, MASK));
        Assert.assertFalse("Match not all bits", isMaskAnd(0b0010, MASK));
    }

    @Test
    public void test$isMaskOr() {
        Assert.assertFalse("Not match", isMaskOr(0b0000, MASK));
        Assert.assertTrue("Match", isMaskOr(0b1010, MASK));
        Assert.assertTrue("Match one bit", isMaskOr(0b1000, MASK));
        Assert.assertTrue("Match one bit", isMaskOr(0b0010, MASK));
    }
}
