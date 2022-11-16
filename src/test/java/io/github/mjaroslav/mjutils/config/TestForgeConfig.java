package io.github.mjaroslav.mjutils.config;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestForgeConfig {
    private static final Path path = Paths.get("TestForgeConfig.cfg");

    private ForgeConfig config;

    @Before
    public void before() {
        config = new ForgeConfig("test", path);
        config.setShouldFailOnError(true);
        config.load();
    }

    @After
    public void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    public void test$registerSyncCallback() {
        config.registerSyncCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        Assert.assertThrows("Expected exception not thrown", RuntimeException.class, config::load);
        Assert.assertThrows("Expected exception not thrown", RuntimeException.class, config::save);
    }

    @Test
    public void test$unregisterSyncCallback() {
        Runnable callback = () -> {
            throw new IllegalStateException("Expected");
        };
        config.registerSyncCallback(callback);
        config.unregisterSyncCallback(callback);
        config.load();
        config.save();
    }
}
