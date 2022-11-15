package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.mod.lib.ModInfo;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Config {
    protected final Set<ConfigCallback> loadCallbacks = new HashSet<>();
    protected final Set<ConfigCallback> saveCallbacks = new HashSet<>();
    protected final @NotNull String modId;
    protected final @NotNull Path file;
    protected final @Nullable String version;
    @Setter
    protected boolean shouldFailOnError = false;

    public Config(@Nullable String modId, @NotNull Path file, @Nullable String version) {
        this.modId = StringUtils.isEmpty(modId) ? UtilsMods.getActiveModId() : modId;
        this.file = file;
        this.version = version;
    }

    public boolean registerLoadCallback(@NotNull ConfigCallback callback) {
        return loadCallbacks.add(callback);
    }

    public boolean registerSaveCallback(@NotNull ConfigCallback callback) {
        return saveCallbacks.add(callback);
    }

    public boolean unregisterLoadCallback(@NotNull ConfigCallback callback) {
        return loadCallbacks.remove(callback);
    }

    public boolean unregisterSaveCallback(@NotNull ConfigCallback callback) {
        return saveCallbacks.remove(callback);
    }

    public final void load() {
        try {
            loadFile();
            if (getVersion() != null && !getVersion().equals(getLoadedVersion())) {
                setDefault();
                saveFile();
            }
            loadCallbacks.forEach(ConfigCallback::call);
        } catch (Exception e) {
            if (isShouldFailOnError())
                throw new RuntimeException(e);
            else ModInfo.loggerLibrary.error("Configuration %s can't be loaded", e, getFile());
        }
    }

    public final void save() {
        try {
            saveFile();
            saveCallbacks.forEach(ConfigCallback::call);
        } catch (Exception e) {
            if (isShouldFailOnError())
                throw new IllegalStateException(e);
            else ModInfo.loggerLibrary.error("Configuration %s can't be saved", e, getFile());
        }
    }

    protected abstract void setDefault() throws Exception;

    protected abstract void loadFile() throws Exception;

    protected abstract void saveFile() throws Exception;

    protected abstract @Nullable String getLoadedVersion();

    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull String modId, @NotNull Path path) {
        return ResourcePath.of(modId, "defaults/" + path.normalize().toAbsolutePath().toString()
            .replace(UtilsMods.getMinecraftDir().toPath().normalize()
                .toAbsolutePath().toString(), ""));
    }

    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull Path path) {
        return resolveDefaultFileResourcePath(UtilsMods.getActiveModId(), path);
    }
}
