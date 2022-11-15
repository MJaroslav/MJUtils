package io.github.mjaroslav.mjutils.util.io;

import lombok.val;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class TestResourcePath {
    @Test
    public void test$equals() {
        val expected = ResourcePath.full("/assets/space/test/to/test.test");
        val location = new ResourceLocation("space:test/to/test.test");
        val ofNamespace = ResourcePath.of("space", "test/to/test.test");
        val of = ResourcePath.of("space:test/to/test.test");
        val ofLocation = ResourcePath.of(location);
        val anotherLocation = new ResourceLocation("anotherspace:another/to/test.test");
        val anotherOfNamespace = ResourcePath.of("anotherspace", "another/to/test.test");
        val anotherOf = ResourcePath.of("anotherspace:another/to/test.test");
        val anotherOfLocation = ResourcePath.of(anotherLocation);
        assertEquals("ofNamespace", expected, ofNamespace);
        assertEquals("of", expected, of);
        assertEquals("ofLocation", expected, ofLocation);
        assertNotEquals("another ofNamespace", expected, anotherOfNamespace);
        assertNotEquals("another of", expected, anotherOf);
        assertNotEquals("another ofLocation", expected, anotherOfLocation);
    }

    @Test
    public void test$input() throws IOException {
        val path = ResourcePath.full("io/github/mjaroslav/mjutils/util/io/TestResourcePathExpected.txt");
        val stream = path.stream();
        assertNotNull("null stream", stream);
        stream.close();
        val bufferedReader = path.bufferedReader();
        assertNotNull("null buffered reader", bufferedReader);
        bufferedReader.close();
        val reader = path.reader();
        assertNotNull("null reader", reader);
        reader.close();

        val actualText = String.join("\n", IOUtils.readLines(path.stream(), StandardCharsets.UTF_8));
        val expectedText = """
            Test file
            with some
            test text""";
        assertEquals("Content", expectedText, actualText);
    }

    @Test
    public void test$create() {
        val ofNamespace = ResourcePath.of("space", "test/to/test.test");
        val of = ResourcePath.of("space:test/to/test.test");
        val full = ResourcePath.full("/assets/space/test/to/test.test");
        val ofLocation = ResourcePath.of(new ResourceLocation("space:test/to/test.test"));
        val expected = "/assets/space/test/to/test.test";
        assertEquals("of with namespace", expected, ofNamespace.getPath());
        assertEquals("of", expected, of.getPath());
        assertEquals("of from location", expected, ofLocation.getPath());
        assertEquals("full", expected, full.getPath());
    }

    @Test
    public void test$toLocation() {
        val expected = new ResourceLocation("space:test/to/test.test");
        val actual = ResourcePath.of(expected).toLocation();
        val actualManual = ResourcePath.full("/assets/space/test/to/test.test").toLocation();
        assertEquals("actual", expected, actual);
        assertEquals("actual manual", expected, actualManual);
    }
}
