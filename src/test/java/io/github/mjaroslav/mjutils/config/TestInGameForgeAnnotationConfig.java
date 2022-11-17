package io.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mcingametester.api.AfterEach;
import com.github.mjaroslav.mcingametester.api.BeforeEach;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import io.github.mjaroslav.mjutils.config.TestInGameForgeAnnotationConfig.MockCategory.MockInnerCategory;
import io.github.mjaroslav.mjutils.config.annotations.Name;
import io.github.mjaroslav.mjutils.config.annotations.Range;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameForgeAnnotationConfig {
    private static final Path path = Paths.get("TestForgeAnnotationConfig.cfg");

    private ForgeAnnotationConfig config;

    @BeforeEach
    void before() {
        config = new ForgeAnnotationConfig("test", path, MockCategory.class);
        config.load();
    }

    @AfterEach
    void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    void test$checkData() {
        val testInt = config.properties.getCategory("general").get("test_int");
        Assert.assertEquals("Values not equals", MockCategory.testInt, testInt.getInt());
        Assert.assertEquals("Ranges not equals", "-10", testInt.getMinValue());
        Assert.assertEquals("Ranges not equals", "20", testInt.getMaxValue());
        val renamed = config.properties.getCategory("general.inner").get("renamed");
        Assert.assertArrayEquals("Values not equals", MockInnerCategory.testBooleanArray, renamed.getBooleanList());
    }

    @Name("general")
    @UtilityClass
    public class MockCategory {
        @Range(min = -10, max = 20)
        public int testInt = 5;

        @UtilityClass
        @Name("inner")
        public class MockInnerCategory {
            @Name("renamed")
            public boolean[] testBooleanArray = new boolean[]{false, true, false};
        }
    }
}
