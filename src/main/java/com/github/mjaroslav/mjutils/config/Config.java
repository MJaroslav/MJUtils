package com.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public abstract class Config {
    protected final Set<Callback> loadCallbacks = new HashSet<>();
    protected final Set<Callback> saveCallbacks = new HashSet<>();
    protected final @NotNull String type;
    protected final @NotNull Path file;

    public boolean registerLoadCallback(@NotNull Callback callback) {
        return loadCallbacks.add(callback);
    }

    public boolean registerSaveCallback(@NotNull Callback callback) {
        return saveCallbacks.add(callback);
    }

    public boolean unregisterLoadCallback(@NotNull Callback callback) {
        return loadCallbacks.remove(callback);
    }

    public boolean unregisterSaveCallback(@NotNull Callback callback) {
        return saveCallbacks.remove(callback);
    }

    public final void load() {
        loadFile(getFile());
        loadCallbacks.forEach(callback -> callback.call(this));
    }

    public final void save() {
        saveFile(getFile());
        saveCallbacks.forEach(callback -> callback.call(this));
    }

    protected abstract void loadFile(@NotNull Path file);

    protected abstract void saveFile(@NotNull Path file);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Config config) return getFile().equals(config.getFile());
        else return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFile());
    }

    @Override
    public String toString() {
        return "Config(type=" + getType() + ", file=[" + getFile() + "]";
    }

    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull String modId, @NotNull Path path) {
        return ResourcePath.of(modId, "defaults/" + path.normalize().toAbsolutePath().toString()
            .replace(UtilsMods.getMinecraftDir().toPath().normalize()
                .toAbsolutePath().toString(), ""));
    }

    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull Path path) {
        return resolveDefaultFileResourcePath(UtilsMods.getActiveModId(), path);
    }
}
