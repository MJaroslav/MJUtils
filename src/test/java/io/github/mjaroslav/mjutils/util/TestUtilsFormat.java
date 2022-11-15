package io.github.mjaroslav.mjutils.util;

import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import static io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat.*;
import static io.github.mjaroslav.mjutils.util.UtilsFormat.*;

public class TestUtilsFormat {
    @Test
    public void test$unpackColorIntToIntArray() {
        val expected = new int[]{0xBB, 0xCC, 0xDD, 0};
        val expectedAlpha = new int[]{0xBB, 0xCC, 0xDD, 0xAA};

        val actualARGB = unpackColorIntToIntArray(0xAABBCCDD, ARGB, RGBA);
        val actualRGB = unpackColorIntToIntArray(0xBBCCDD, RGB, RGBA);
        val actualRGBA = unpackColorIntToIntArray(0xBBCCDDAA, RGBA, RGBA);
        val actualBGRA = unpackColorIntToIntArray(0xDDCCBBAA, BGRA, RGBA);
        val actualBGR = unpackColorIntToIntArray(0xAADDCCBB, BGR, RGBA);
        val actualABGR = unpackColorIntToIntArray(0xAADDCCBB, ABGR, RGBA);

        Assert.assertArrayEquals("ARGB", expectedAlpha, actualARGB);
        Assert.assertArrayEquals("RGB", expected, actualRGB);
        Assert.assertArrayEquals("RGBA", expectedAlpha, actualRGBA);
        Assert.assertArrayEquals("BGRA", expectedAlpha, actualBGRA);
        Assert.assertArrayEquals("BGR", expected, actualBGR);
        Assert.assertArrayEquals("ABGR", expectedAlpha, actualABGR);
    }

    @Test
    public void test$unpackColorIntToFloatArray() {
        val expected = new float[]{0xBB / 255F, 0xCC / 255F, 0xDD / 255F, 0};
        val expectedAlpha = new float[]{0xBB / 255F, 0xCC / 255F, 0xDD / 255F, 0xAA / 255F};

        val actualARGB = unpackColorIntToFloatArray(0xAABBCCDD, ARGB, RGBA);
        val actualRGB = unpackColorIntToFloatArray(0xBBCCDD, RGB, RGBA);
        val actualRGBA = unpackColorIntToFloatArray(0xBBCCDDAA, RGBA, RGBA);
        val actualBGRA = unpackColorIntToFloatArray(0xDDCCBBAA, BGRA, RGBA);
        val actualBGR = unpackColorIntToFloatArray(0xAADDCCBB, BGR, RGBA);
        val actualABGR = unpackColorIntToFloatArray(0xAADDCCBB, ABGR, RGBA);

        Assert.assertArrayEquals("ARGB", expectedAlpha, actualARGB, 0F);
        Assert.assertArrayEquals("RGB", expected, actualRGB, 0F);
        Assert.assertArrayEquals("RGBA", expectedAlpha, actualRGBA, 0F);
        Assert.assertArrayEquals("BGRA", expectedAlpha, actualBGRA, 0F);
        Assert.assertArrayEquals("BGR", expected, actualBGR, 0F);
        Assert.assertArrayEquals("ABGR", expectedAlpha, actualABGR, 0F);
    }

    @Test
    public void test$unpackColorIntToDoubleArray() {
        val expected = new double[]{0xBB / 255D, 0xCC / 255D, 0xDD / 255D, 0};
        val expectedAlpha = new double[]{0xBB / 255D, 0xCC / 255D, 0xDD / 255D, 0xAA / 255D};

        val actualARGB = unpackColorIntToDoubleArray(0xAABBCCDD, ARGB, RGBA);
        val actualRGB = unpackColorIntToDoubleArray(0xBBCCDD, RGB, RGBA);
        val actualRGBA = unpackColorIntToDoubleArray(0xBBCCDDAA, RGBA, RGBA);
        val actualBGRA = unpackColorIntToDoubleArray(0xDDCCBBAA, BGRA, RGBA);
        val actualBGR = unpackColorIntToDoubleArray(0xAADDCCBB, BGR, RGBA);
        val actualABGR = unpackColorIntToDoubleArray(0xAADDCCBB, ABGR, RGBA);

        Assert.assertArrayEquals("ARGB", expectedAlpha, actualARGB, 0F);
        Assert.assertArrayEquals("RGB", expected, actualRGB, 0F);
        Assert.assertArrayEquals("RGBA", expectedAlpha, actualRGBA, 0F);
        Assert.assertArrayEquals("BGRA", expectedAlpha, actualBGRA, 0F);
        Assert.assertArrayEquals("BGR", expected, actualBGR, 0F);
        Assert.assertArrayEquals("ABGR", expectedAlpha, actualABGR, 0F);
    }

    @Test
    public void test$packToColorInt() {
        val expected = 0xDDCCBBAA;

        val actualInt = packToColorInt(0xAA, 0xBB, 0xCC, 0xDD, BGRA);
        val actualFloat = packToColorInt(0xAA / 255F, 0xBB / 255F, 0xCC / 255F, 0xDD / 255F, BGRA);
        val actualDouble = packToColorInt(0xAA / 255D, 0xBB / 255D, 0xCC / 255D, 0xDD / 255D, BGRA);

        Assert.assertEquals("int", expected, actualInt);
        Assert.assertEquals("float", expected, actualFloat);
        Assert.assertEquals("double", expected, actualDouble);
    }

    @Test
    public void test$isMask() {
        Assert.assertFalse("Not match", isMask(0b000, 0b010));
        Assert.assertTrue("Match", isMask(0b010, 0b010));
    }
}
