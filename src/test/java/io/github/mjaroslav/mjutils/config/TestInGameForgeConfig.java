package io.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mcingametester.api.AfterEach;
import com.github.mjaroslav.mcingametester.api.BeforeEach;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import net.minecraft.util.ReportedException;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameForgeConfig {
    private static final Path path = Paths.get("TestForgeConfig.cfg");

    private ForgeConfig config;

    @BeforeEach
    void before() {
        config = new ForgeConfig("test", path);
        config.setShouldFailOnError(true);
        config.load();
    }

    @AfterEach
    void after() throws IOException {
        Files.deleteIfExists(path);
    }

    @Test
    void test$registerSyncCallback() {
        config.registerSyncCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        Assert.assertThrows("Expected exception not thrown", ReportedException.class, config::load);
        Assert.assertThrows("Expected exception not thrown", ReportedException.class, config::save);
    }

    @Test
    void test$unregisterSyncCallback() {
        Runnable callback = () -> {
            throw new IllegalStateException("Expected");
        };
        config.registerSyncCallback(callback);
        config.unregisterSyncCallback(callback);
        config.load();
        config.save();
    }
}
