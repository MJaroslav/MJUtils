package io.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.LoaderState;
import lombok.val;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import java.nio.file.Paths;

@Common(when = LoaderState.CONSTRUCTING)
public class TestInGameConfig {
    @Test
    void test$registerLoadCallback() {
        val config = createMock();
        config.registerLoadCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        Assert.assertThrows("Expected exception not thrown", ReportedException.class, config::load);
        config.save();
    }

    @Test
    void test$registerSaveCallback() {
        val config = createMock();
        config.registerSaveCallback(() -> {
            throw new IllegalStateException("Expected");
        });
        config.load();
        Assert.assertThrows("Expected exception not thrown", ReportedException.class, config::save);
    }

    @Test
    void test$unregisterLoadCallback() {
        val config = createMock();
        Runnable callback = () -> {
            throw new IllegalStateException("Expected");
        };
        config.registerLoadCallback(callback);
        config.unregisterLoadCallback(callback);
        config.load();
        config.save();
    }

    @Test
    void test$unregisterSaveCallback() {
        val config = createMock();
        Runnable callback = () -> {
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
            protected void restoreDefaultFile() {
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
