package com.github.mjaroslav.mjutils.test.unit.config;

import com.github.mjaroslav.mjutils.config.PropertiesConfig;
import com.github.mjaroslav.mjutils.mod.lib.ModInfo;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestPropertiesConfig {
    private static final ResourcePath resourcePath = ResourcePath.of(ModInfo.modId + ":test/TestPropertiesConfig.properties");
    private static final Path path = Paths.get("TestPropertiesConfig.properties");
    private static final PropertiesConfig config = new PropertiesConfig(path);

    @BeforeClass
    public static void before() throws Exception {
        Files.copy(Objects.requireNonNull(resourcePath.stream()), path);
        config.load();
    }

    @AfterClass
    public static void after() throws Exception {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$getString() {
        Assert.assertEquals("String value comment", "# String value comment", config.getComment("value.string"));
        Assert.assertEquals("String value", "String value", config.getString("value.string"));
    }

    @Test
    public void test$getStringArray() {
        Assert.assertArrayEquals("String array value comment", new String[]{"# Multi line", "# value comment"},
            Objects.requireNonNull(config.getComment("value.string.array")).split("\\r?\\n"));
        Assert.assertArrayEquals("String array value", new String[]{"String", "value", "array"},
            config.getStringArray("value.string.array"));
        Assert.assertNull("One element String array value comment", config.getComment("value.string.array.one"));
        Assert.assertArrayEquals("One element String array value", new String[]{"One element array"},
            config.getStringArray("value.string.array.one"));
    }

    @Test
    public void test$getInt() {
        Assert.assertNull("int value comment", config.getComment("value.int"));
        Assert.assertEquals("int value", 15, config.getInt("value.int"));
    }

    @Test
    public void test$getIntArray() {
        Assert.assertNull("int array value comment", config.getComment("value.int.array"));
        Assert.assertArrayEquals("int array value", new int[]{-10, 6, 300},
            config.getIntArray("value.int.array"));
        Assert.assertNull("One element int array value comment", config.getComment("value.int.array.one"));
        Assert.assertArrayEquals("One element int array value", new int[]{30},
            config.getIntArray("value.int.array.one"));
    }

    @Test
    public void test$getLong() {
        Assert.assertNull("long value comment", config.getComment("value.long"));
        Assert.assertEquals("long value", 34359738368L, config.getLong("value.long"));
    }

    @Test
    public void test$getLongArray() {
        Assert.assertNull("long array value comment", config.getComment("value.long.array"));
        Assert.assertArrayEquals("long array value", new long[]{34359738368L, -34359738368L},
            config.getLongArray("value.long.array"));
        Assert.assertNull("One element long array value comment", config.getComment("value.long.array.one"));
        Assert.assertArrayEquals("One element long array value", new long[]{34359738368L},
            config.getLongArray("value.long.array.one"));
    }

    @Test
    public void test$getBoolean() {
        Assert.assertNull("boolean value comment", config.getComment("value.boolean"));
        Assert.assertTrue("boolean value", config.getBoolean("value.boolean"));
    }

    @Test
    public void test$getBooleanArray() {
        Assert.assertNull("boolean array value comment", config.getComment("value.boolean.array"));
        Assert.assertArrayEquals("boolean array value", new boolean[]{false, true, false},
            config.getBooleanArray("value.boolean.array"));
        Assert.assertNull("One element boolean array value comment", config.getComment("value.boolean.array.one"));
        Assert.assertArrayEquals("One element boolean array value", new boolean[]{false},
            config.getBooleanArray("value.boolean.array.one"));
    }

    @Test
    public void test$getFloat() {
        Assert.assertNull("float value comment", config.getComment("value.float"));
        Assert.assertEquals("float value", 6.5f, config.getFloat("value.float"), 0f);
    }

    @Test
    public void test$getFloatArray() {
        Assert.assertNull("float array value comment", config.getComment("value.float.array"));
        Assert.assertArrayEquals("float array value", new float[]{-10.5f, 0.123f, 300f},
            config.getFloatArray("value.float.array"), 0f);
        Assert.assertNull("One element float array value comment", config.getComment("value.float.array.one"));
        Assert.assertArrayEquals("One element float array value", new float[]{41f},
            config.getFloatArray("value.float.array.one"), 0f);
    }

    @Test
    public void test$getDouble() {
        Assert.assertNull("double value comment", config.getComment("value.double"));
        Assert.assertEquals("double value", 15.123321d, config.getDouble("value.double"), 0d);
    }

    @Test
    public void test$getDoubleArray() {
        Assert.assertNull("double array value comment", config.getComment("value.double.array"));
        Assert.assertArrayEquals("double array value", new double[]{-10.123123d, 0.0d, -300d},
            config.getDoubleArray("value.double.array"), 0d);
        Assert.assertNull("One element double array value comment", config.getComment("value.double.array.one"));
        Assert.assertArrayEquals("One element double array value", new double[]{-10.123123d},
            config.getDoubleArray("value.double.array.one"), 0d);
    }

    @Test
    public void test$setValue() {
        config.setValue("set.string", "String value", "Test comment");
        Assert.assertEquals("Set string value comment", "Test comment", config.getComment("set.string"));
        Assert.assertEquals("Set string value", "String value", config.getString("set.string"));

        config.setValue("set.double.array", new Object[]{5.1d, -6.123d, 0.0d}, "Test multiline\nComment");
        Assert.assertArrayEquals("Set double array value comment", new String[]{"Test multiline", "Comment"},
            Objects.requireNonNull(config.getComment("set.double.array")).split("\\r?\\n"));
        Assert.assertArrayEquals("Set double array value", new double[]{5.1d, -6.123d, 0.0d},
            config.getDoubleArray("set.double.array"), 0d);

        config.setValue("set.double.array.one", new Object[]{-5.5d});
        Assert.assertNull("Set one element double array value comment", config.getComment("set.double.array.one"));
        Assert.assertArrayEquals("Set one element double array value", new double[]{-5.5d},
            config.getDoubleArray("set.double.array.one"), 0d);
    }
}
