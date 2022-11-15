package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.config.TestForgeAnnotationConfig.MockCategory.MockInnerCategory;
import io.github.mjaroslav.mjutils.config.annotations.Name;
import io.github.mjaroslav.mjutils.config.annotations.Range;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestForgeAnnotationConfig {
    private static final Path path = Paths.get("TestForgeAnnotationConfig.cfg");

    private ForgeAnnotationConfig config;

    @BeforeClass
    public static void beforeClass() {
        // Forge moment, bruh
        ReflectionHelper.setPrivateValue(FMLInjectionData.class, null, new File("."), "minecraftHome");
    }

    @Before
    public void before() {
        config = new ForgeAnnotationConfig("test", path, MockCategory.class);
        config.load();
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$checkData() {
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
