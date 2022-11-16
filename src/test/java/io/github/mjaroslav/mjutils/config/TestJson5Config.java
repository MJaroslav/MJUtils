package io.github.mjaroslav.mjutils.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.SerializedName;
import io.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class TestJson5Config {
    private static final ResourcePath resourcePath = ResourcePath.full("/io/github/mjaroslav/mjutils/config/TestJson5Config.json5");
    private static final ResourcePath defaultPath = ResourcePath.full("/io/github/mjaroslav/mjutils/config/TestJson5ConfigDefault.json5");
    private static final Path path = Paths.get("TestJson5Config.json5");

    private Json5Config config;

    @Before
    public void before() throws IOException {
        Files.copy(Objects.requireNonNull(resourcePath.stream()), path);
        config = new Json5Config("test", path);
        config.load();
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$value() {
        val expected = createMock();
        var actual = config.getValue();
        Assert.assertEquals("JsonObjects not equals", expected, actual);
        config.getValue().put("integer", JsonPrimitive.of(-1L));
        actual = config.getValue();
        Assert.assertNotEquals("JsonObjects are equals", expected, actual);
        expected.put("integer", JsonPrimitive.of(-1L));
        Assert.assertEquals("JsonObjects not equals", expected, actual);
    }

    @Test
    public void test$get() {
        val expected = new GenericMock();
        val actual = config.get(GenericMock.class);
        Assert.assertEquals("Generics not equals", expected, actual);
    }

    @Test
    public void test$set() {
        val expected = new GenericMock();
        var actual = config.get(GenericMock.class);
        Assert.assertEquals("Generics not equals", expected, actual);
        config.getValue().put("integer", JsonPrimitive.of(-1L));
        actual = config.get(GenericMock.class);
        Assert.assertNotEquals("Generics are equals", expected, actual);
        expected.integer = -1;
        Assert.assertEquals("Generics not equals", expected, actual);
    }

    @Test
    public void test$save() throws IOException {
        Files.deleteIfExists(path);
        config.save();
        Assert.assertTrue("File not created", Files.isRegularFile(path));
        val expected = config.getValue();
        val configForLoad = new Json5Config("test", path);
        configForLoad.load();
        val actual = configForLoad.getValue();
        Assert.assertEquals("Saved value not equals", expected, actual);
    }

    @Test
    public void test$setDefault() throws Exception {
        val config = new Json5Config("test", path, null, defaultPath);
        config.restoreDefaultFile();
        val actual = config.getValue();
        val expected = new JsonObject();
        expected.put(Json5Config.VERSION_KEY, JsonPrimitive.of("1"));
        expected.put("default_value", JsonPrimitive.of("value"));
        Assert.assertEquals("JsonObjects not equals", expected, actual);
    }

    @Test
    public void test$version() {
        config.setValue(createMock());
        config.save();
        val configForLoad = new Json5Config("test", path, "1", defaultPath);
        configForLoad.load();
        val actual = configForLoad.getValue();
        val expected = new JsonObject();
        expected.put(Json5Config.VERSION_KEY, JsonPrimitive.of("1"));
        expected.put("default_value", JsonPrimitive.of("value"));
        Assert.assertEquals("JsonObjects not equals", expected, actual);
    }

    private @NotNull JsonObject createMock() {
        val root = new JsonObject();
        root.put("integer", JsonPrimitive.of(1L), "Test comment");
        root.put("string", JsonPrimitive.of("string value"));
        root.put("double", JsonPrimitive.of(10.5));
        val inner = new JsonObject();
        val array = new JsonArray();
        array.add(JsonPrimitive.of(1L));
        array.add(JsonPrimitive.of(2L));
        array.add(JsonPrimitive.of(3L));
        inner.put("array", array);
        root.put("inner", inner);
        return root;
    }

    @ToString
    @EqualsAndHashCode
    private static class GenericMock {
        @Comment("Test comment")
        int integer = 1;
        String string = "string value";
        @SerializedName("double")
        double doubleValue = 10.5;
        GenericInnerMock inner = new GenericInnerMock();

        @ToString
        @EqualsAndHashCode
        private static class GenericInnerMock {
            int[] array = new int[]{1, 2, 3};
        }
    }
}
