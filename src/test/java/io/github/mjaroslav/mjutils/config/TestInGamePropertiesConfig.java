package io.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mcingametester.api.AfterEach;
import com.github.mjaroslav.mcingametester.api.BeforeEach;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import io.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

import static org.junit.Assert.*;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGamePropertiesConfig {
    private static final ResourcePath resourcePath = ResourcePath.full("io/github/mjaroslav/mjutils/config/TestPropertiesConfig.properties");
    private static final ResourcePath expectedResourcePath = ResourcePath.full("io/github/mjaroslav/mjutils/config/TestPropertiesConfigExpected.properties");
    private static final ResourcePath defaultPath = ResourcePath.full("/io/github/mjaroslav/mjutils/config/TestPropertiesConfigDefault.properties");
    private static final Path path = Paths.get("TestPropertiesConfig.properties");

    private PropertiesConfig config;

    @BeforeEach
    void before() throws IOException {
        Files.copy(Objects.requireNonNull(resourcePath.stream()), path);
        config = new PropertiesConfig("test", path);
        config.load();
    }

    @AfterEach
    void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    void test$getString() {
        assertEquals("String value comment", "String value comment", config.getComment("value.string"));
        assertEquals("String value", "String value", config.getString("value.string"));
    }

    @Test
    void test$getStringArray() {
        assertArrayEquals("String array value comment", new String[]{"Multi line", "value comment"},
            Objects.requireNonNull(config.getComment("value.string.array")).split(System.lineSeparator()));
        assertArrayEquals("String array value", new String[]{"String", "value", "array"},
            config.getStringArray("value.string.array"));
        assertNull("One element String array value comment", config.getComment("value.string.array.one"));
        assertArrayEquals("One element String array value", new String[]{"One element array"},
            config.getStringArray("value.string.array.one"));
    }

    @Test
    void test$getInt() {
        assertNull("int value comment", config.getComment("value.int"));
        assertEquals("int value", 15, config.getInt("value.int"));
    }

    @Test
    void test$getIntArray() {
        assertNull("int array value comment", config.getComment("value.int.array"));
        assertArrayEquals("int array value", new int[]{-10, 6, 300},
            config.getIntArray("value.int.array"));
        assertNull("One element int array value comment", config.getComment("value.int.array.one"));
        assertArrayEquals("One element int array value", new int[]{30},
            config.getIntArray("value.int.array.one"));
    }

    @Test
    void test$getLong() {
        assertNull("long value comment", config.getComment("value.long"));
        assertEquals("long value", 34359738368L, config.getLong("value.long"));
    }

    @Test
    void test$getLongArray() {
        assertNull("long array value comment", config.getComment("value.long.array"));
        assertArrayEquals("long array value", new long[]{34359738368L, -34359738368L},
            config.getLongArray("value.long.array"));
        assertNull("One element long array value comment", config.getComment("value.long.array.one"));
        assertArrayEquals("One element long array value", new long[]{34359738368L},
            config.getLongArray("value.long.array.one"));
    }

    @Test
    void test$getBoolean() {
        assertNull("boolean value comment", config.getComment("value.boolean"));
        Assert.assertTrue("boolean value", config.getBoolean("value.boolean"));
    }

    @Test
    void test$getBooleanArray() {
        assertNull("boolean array value comment", config.getComment("value.boolean.array"));
        assertArrayEquals("boolean array value", new boolean[]{false, true, false},
            config.getBooleanArray("value.boolean.array"));
        assertNull("One element boolean array value comment", config.getComment("value.boolean.array.one"));
        assertArrayEquals("One element boolean array value", new boolean[]{false},
            config.getBooleanArray("value.boolean.array.one"));
    }

    @Test
    void test$getFloat() {
        assertNull("float value comment", config.getComment("value.float"));
        assertEquals("float value", 6.5f, config.getFloat("value.float"), 0f);
    }

    @Test
    void test$getFloatArray() {
        assertNull("float array value comment", config.getComment("value.float.array"));
        assertArrayEquals("float array value", new float[]{-10.5f, 0.123f, 300f},
            config.getFloatArray("value.float.array"), 0f);
        assertNull("One element float array value comment", config.getComment("value.float.array.one"));
        assertArrayEquals("One element float array value", new float[]{41f},
            config.getFloatArray("value.float.array.one"), 0f);
    }

    @Test
    void test$getDouble() {
        assertNull("double value comment", config.getComment("value.double"));
        assertEquals("double value", 15.123321d, config.getDouble("value.double"), 0d);
    }

    @Test
    void test$getDoubleArray() {
        assertNull("double array value comment", config.getComment("value.double.array"));
        assertArrayEquals("double array value", new double[]{-10.123123d, 0.0d, -300d},
            config.getDoubleArray("value.double.array"), 0d);
        assertNull("One element double array value comment", config.getComment("value.double.array.one"));
        assertArrayEquals("One element double array value", new double[]{-10.123123d},
            config.getDoubleArray("value.double.array.one"), 0d);
    }

    @Test
    void test$setValue() {
        config.setValue("set.string", "String value", "Test comment");
        assertEquals("Set string value comment", "Test comment", config.getComment("set.string"));
        assertEquals("Set string value", "String value", config.getString("set.string"));
        config.setValue("set.double.array", new Object[]{5.1d, -6.123d, 0.0d}, "Test multiline\nComment");
        assertArrayEquals("Set double array value comment", new String[]{"Test multiline", "Comment"},
            Objects.requireNonNull(config.getComment("set.double.array")).split(System.lineSeparator()));
        assertArrayEquals("Set double array value", new double[]{5.1d, -6.123d, 0.0d},
            config.getDoubleArray("set.double.array"), 0d);
        config.setValue("set.double.array.one", new Object[]{-5.5d});
        assertNull("Set one element double array value comment", config.getComment("set.double.array.one"));
        assertArrayEquals("Set one element double array value", new double[]{-5.5d},
            config.getDoubleArray("set.double.array.one"), 0d);
    }

    @Test
    void test$save() throws IOException {
        config.setValue("set.string", "String value", "Test comment");
        config.setValue("set.double.array", new Object[]{5.1d, -6.123d, 0.0d}, "Test multiline\nComment");
        config.setValue("set.double.array.one", new Object[]{-5.5d});
        config.save();
        // Dodging different line separators
        val expected = String.join(System.lineSeparator(),
            IOUtils.readLines(Objects.requireNonNull(expectedResourcePath.stream()), StandardCharsets.UTF_8));
        val actual = String.join(System.lineSeparator(),
            Files.readAllLines(path, StandardCharsets.UTF_8));
        assertEquals("Different files", expected, actual);
    }

    @Test
    void test$setComment() {
        config.setComment("value.string", "Test comment");
        assertEquals("Different comments", "Test comment", config.getComment("value.string"));
    }

    @Test
    void test$setDefault() throws IOException {
        val config = new PropertiesConfig("test", path, null, defaultPath);
        config.setValue("default.value", "bad_value");
        config.restoreDefaultFile();
        val expected = new Properties();
        expected.setProperty(PropertiesConfig.VERSION_KEY, "1");
        expected.setProperty("default.value", "value");
        Assert.assertEquals("Properties not equals", expected, config.values);
    }

    @Test
    void test$version() throws IOException {
        config.setValue("version", "2");
        config.save();
        val config = new PropertiesConfig("test", path, null, defaultPath);
        config.restoreDefaultFile();
        val expected = new Properties();
        expected.setProperty(PropertiesConfig.VERSION_KEY, "1");
        expected.setProperty("default.value", "value");
        Assert.assertEquals("Properties not equals", expected, config.values);
    }
}
