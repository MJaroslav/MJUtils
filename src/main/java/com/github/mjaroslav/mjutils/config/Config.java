package com.github.mjaroslav.mjutils.config;

import com.github.mjaroslav.mjutils.util.game.UtilsMods;
import com.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class Config {
    protected final Set<Callback> loadCallbacks = new HashSet<>();
    protected final Set<Callback> saveCallbacks = new HashSet<>();

    @Getter
    protected final @NotNull String type;

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

    public void load() {
        for (var file : getFiles()) loadFile(file);
        loadCallbacks.forEach(callback -> callback.call(this));
    }


    public void save() {
        for (var file : getFiles()) saveFile(file);
        saveCallbacks.forEach(callback -> callback.call(this));
    }

    protected abstract @NotNull Path @NotNull [] getFiles();

    protected abstract void loadFile(@NotNull Path file);

    protected abstract void saveFile(@NotNull Path file);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Config config) return Arrays.equals(getFiles(), config.getFiles());
        else return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getFiles());
    }

    public String toString() {
        return "Config(type=" + type + ", files=[" +
            Arrays.stream(getFiles()).map(Path::toString).collect(Collectors.joining(", ")) + "]";
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
