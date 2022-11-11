package com.github.mjaroslav.mjutils.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.annotation.SerializedName;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
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
    private static final ResourcePath resourcePath = ResourcePath.full("com/github/mjaroslav/mjutils/config/TestJson5Config.json5");
    private static final Path path = Paths.get("TestJson5Config.json5");
    private static final Json5Config config = new Json5Config(path);

    @Before
    public void before() throws IOException {
        Files.copy(Objects.requireNonNull(resourcePath.stream()), path);
        config.load();
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$value() {
        val pattern = createPattern();
        Assert.assertEquals("JsonObjects not equals", pattern, config.getValue());
        config.getValue().put("integer", JsonPrimitive.of(-1L));
        Assert.assertNotEquals("JsonObjects are equals", pattern, config.getValue());
        pattern.put("integer", JsonPrimitive.of(-1L));
        Assert.assertEquals("JsonObjects not equals", pattern, config.getValue());
    }

    @Test
    public void test$get() {
        Assert.assertEquals("Generics not equals", new TestGeneric(), config.get(TestGeneric.class));
    }

    @Test
    public void test$set() {
        val pattern = new TestGeneric();
        Assert.assertEquals("Generics not equals", pattern, config.get(TestGeneric.class));
        config.getValue().put("integer", JsonPrimitive.of(-1L));
        Assert.assertNotEquals("Generics are equals", pattern, config.get(TestGeneric.class));
        pattern.integer = -1;
        Assert.assertEquals("Generics not equals", pattern, config.get(TestGeneric.class));
    }

    @Test
    public void test$save() throws IOException {
        Files.deleteIfExists(path);
        config.save();
        Assert.assertTrue("File not created", Files.isRegularFile(path));
        val actual = new Json5Config(path);
        actual.load();
        Assert.assertEquals("Saved value not equals", config.getValue(), actual.getValue());
    }

    @Test
    public void test$setDefault() throws IOException {
        val config = new Json5Config(path, null, ResourcePath.full("/com/github/mjaroslav/mjutils/config/TestJson5ConfigDefault.json5"));
        config.setDefault();
        val root = new JsonObject();
        root.put(Json5Config.VERSION_KEY, JsonPrimitive.of("1"));
        root.put("default_value", JsonPrimitive.of("value"));
        Assert.assertEquals("JsonObjects not equals", root, config.getValue());
    }

    @Test
    public void test$version() throws IOException {
        config.setValue(createPattern());
        config.save();
        val config = new Json5Config(path, "1", ResourcePath.full("/com/github/mjaroslav/mjutils/config/TestJson5ConfigDefault.json5"));
        config.load();
        val root = new JsonObject();
        root.put(Json5Config.VERSION_KEY, JsonPrimitive.of("1"));
        root.put("default_value", JsonPrimitive.of("value"));
        Assert.assertEquals("JsonObjects not equals", root, config.getValue());
    }

    private @NotNull JsonObject createPattern() {
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
    private static class TestGeneric {
        @Comment("Test comment")
        int integer = 1;
        String string = "string value";
        @SerializedName("double")
        double doubleValue = 10.5;
        TestGenericInner inner = new TestGenericInner();

        @ToString
        @EqualsAndHashCode
        private static class TestGenericInner {
            int[] array = new int[]{1, 2, 3};
        }
    }
}
