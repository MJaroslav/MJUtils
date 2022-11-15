package com.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mjutils.config.TestGeneralCategory.InnerCategory;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.val;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestForgeAnnotationConfig {
    private static final Path path = Paths.get("TestForgeAnnotationConfig.cfg");
    private static ForgeAnnotationConfig config;

    @BeforeClass
    public static void beforeClass() {
        ReflectionHelper.setPrivateValue(FMLInjectionData.class, null, new File("."), "minecraftHome");
        config = new ForgeAnnotationConfig("test", path, TestGeneralCategory.class);
        config.load();
    }

    @AfterClass
    public static void afterClass() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$checkData() {
        val testInt = config.properties.getCategory("general").get("test_int");
        Assert.assertEquals("Values not equals", TestGeneralCategory.testInt, testInt.getInt());
        Assert.assertEquals("Ranges not equals", "-10", testInt.getMinValue());
        Assert.assertEquals("Ranges not equals", "20", testInt.getMaxValue());
        val renamed = config.properties.getCategory("general.inner").get("renamed");
        Assert.assertArrayEquals("Values not equals", InnerCategory.testBooleanArray, renamed.getBooleanList());
    }
}
