package com.github.mjaroslav.mjutils.config;

import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Paths;

public class TestConfig {
    @Test
    public void test$registerLoadCallback() {
        val config = createMock();
        config.registerLoadCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        Assert.assertThrows("Expected exception not thrown", RuntimeException.class, config::load);
        config.save();
    }

    @Test
    public void test$registerSaveCallback() {
        val config = createMock();
        config.registerSaveCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        config.load();
        Assert.assertThrows("Expected exception not thrown", RuntimeException.class, config::save);
    }

    @Test
    public void test$unregisterLoadCallback() {
        val config = createMock();
        ConfigCallback callback = () -> {
            throw new IllegalStateException("Expected");
        };
        config.registerLoadCallback(callback);
        config.unregisterLoadCallback(callback);
        config.load();
        config.save();
    }

    @Test
    public void test$unregisterSaveCallback() {
        val config = createMock();
        ConfigCallback callback = () -> {
            throw new IllegalStateException("Expected");
        };
        config.registerSaveCallback(callback);
        config.unregisterSaveCallback(callback);
        config.load();
        config.save();
    }

    @Contract(" -> new")
    private @NotNull Config createMock() {
        return new Config("test", Paths.get("test.cfg"), null) {
            @Override
            public boolean isShouldFailOnError() {
                return true;
            }

            @Override
            protected void setDefault() {
            }

            @Override
            protected void loadFile() {
            }

            @Override
            protected void saveFile() {
            }

            @Override
            protected @Nullable String getLoadedVersion() {
                return null;
            }
        };
    }
}
