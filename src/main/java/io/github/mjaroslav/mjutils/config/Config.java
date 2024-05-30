package io.github.mjaroslav.mjutils.config;

import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.mjutils.util.UtilsDesktop;
import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.mjutils.util.object.game.ResourcePath;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Base config for realization. It contains already implemented or entry points to implement common logic such as
 * loading and saving, versioning and restoring to default, adding extra logic after some actions by callbacks.
 * <br>
 * Library already have some implementations.
 *
 * @see PropertiesConfig
 * @see Json5Config
 * @see ForgeConfig
 * @see ForgeAnnotationConfig
 */
@Getter
public abstract class Config {
    protected final Set<Runnable> loadCallbacks = new LinkedHashSet<>();
    protected final Set<Runnable> saveCallbacks = new LinkedHashSet<>();
    protected final @NotNull String modId;
    protected final @NotNull Path file;
    protected final @Nullable String version;
    /**
     * Crash game if config can't be load or saved.
     */
    @Setter
    protected boolean shouldFailOnError = false;

    /**
     * @param modId   internal owner ID of this config if not present then will be resolved automatically.
     * @param file    file for config saving.
     * @param version implemented version of this config, you can use it for handing cases when the loaded
     *                version differs from the implemented.
     */
    public Config(@Nullable String modId, @NotNull Path file, @Nullable String version) {
        this.modId = StringUtils.isEmpty(modId) ? UtilsMods.getActiveModId() : modId;
        this.file = file;
        this.version = version;
    }

    /**
     * Adds extra logic after each config load.
     *
     * @param callback functional interface with logic.
     * @return true if this config did not already contain the specified callback.
     * @see Config#unregisterLoadCallback(Runnable)
     */
    public boolean registerLoadCallback(@NotNull Runnable callback) {
        return loadCallbacks.add(callback);
    }

    /**
     * Adds extra logic after each config save.
     *
     * @param callback functional interface with logic.
     * @return true if this config did not already contain the specified callback.
     * @see Config#unregisterSaveCallback(Runnable)
     */
    public boolean registerSaveCallback(@NotNull Runnable callback) {
        return saveCallbacks.add(callback);
    }

    /**
     * Removes extra logic after each config load.
     *
     * @param callback already registered callback for unregister.
     * @return true if this config contained the specified callback.
     * @see Config#registerLoadCallback(Runnable)
     */
    public boolean unregisterLoadCallback(@NotNull Runnable callback) {
        return loadCallbacks.remove(callback);
    }

    /**
     * Removes extra logic after each config save.
     *
     * @param callback already registered callback for unregister.
     * @return true if this config contained the specified callback.
     * @see Config#registerSaveCallback(Runnable)
     */
    public boolean unregisterSaveCallback(@NotNull Runnable callback) {
        return saveCallbacks.remove(callback);
    }

    /**
     * Load values from config file and run load callbacks.
     */
    public final void load() {
        try {
            loadFile();
            val version = getVersion();
            if (StringUtils.isNotEmpty(version) && !version.equals(getLoadedVersion())) {
                restoreDefaultFile();
                saveFile();
            }
            loadCallbacks.forEach(Runnable::run);
        } catch (Exception e) {
            if (isShouldFailOnError()) UtilsDesktop.crashGame(e, "Configuration " + getFile() + "can't be load");
            else MJUtilsInfo.LOG_LIB.error("Configuration {} can't be load", getFile(), e);
        }
    }

    /**
     * Save values to config file and run save callbacks.
     */
    public final void save() {
        try {
            saveFile();
            saveCallbacks.forEach(Runnable::run);
        } catch (Exception e) {
            if (isShouldFailOnError()) UtilsDesktop.crashGame(e, "Configuration " + getFile() + " can't be saved");
            else {
                MJUtilsInfo.LOG_LIB.error("Configuration {} can't be saved", getFile());
                MJUtilsInfo.LOG_LIB.error(e);
            }
        }
    }

    /**
     * Restore values to default and save to config file.
     */
    public final void restoreDefault() {
        try {
            restoreDefaultFile();
        } catch (Exception e) {
            if (isShouldFailOnError())
                UtilsDesktop.crashGame(e, "Configuration " + getFile() + " can't be restored to default values");
            else MJUtilsInfo.LOG_LIB.error("Configuration {} can't be restored to default values", getFile(), e);
        }
    }

    /**
     * Restore default values and save to config file (by calling {@link Config#saveFile()} for example).
     * Best practice is a calling this when another config version loaded. You can not implement this logic if you want.
     *
     * @throws Exception Just throw exception with reason if you can't load config.
     */
    protected abstract void restoreDefaultFile() throws Exception;

    /**
     * Read values from config file, you should handle file no existing and similar things by yourself.
     * You also should handle difference of loaded and implemented version of configurations if this happens.
     *
     * @throws Exception Just throw exception with reason if you can't load config.
     */
    protected abstract void loadFile() throws Exception;

    /**
     * Save values to config file, you should create parent directories and similar things by yourself.
     *
     * @throws Exception Just throw exception with reason if you can't save config.
     */
    protected abstract void saveFile() throws Exception;

    /**
     * Get config version from loaded file.
     *
     * @return Loaded version or null if versions logic not implemented.
     */
    protected abstract @Nullable String getLoadedVersion();

    /**
     * Create {@link ResourcePath} for specified internal config in assets.
     *
     * @param modId assets domain
     * @param path  path to config, part to config dir will br removed.
     * @return new resource path in format assets/modId/defaults/path
     */
    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull String modId, @NotNull Path path) {
        return ResourcePath.of(modId, "defaults/" + path.normalize().toAbsolutePath().toString()
            .replace(UtilsMods.getMinecraftDir().toPath().normalize()
                .toAbsolutePath().toString(), ""));
    }

    /**
     * Create {@link ResourcePath} for specified internal config in assets. Assets domain will be resolved automatically.
     *
     * @param path path to config, part to config dir will remove.
     * @return new resource path in format assets/modId/defaults/path
     * @see Config#resolveDefaultFileResourcePath(String, Path)
     */
    public static @NotNull ResourcePath resolveDefaultFileResourcePath(@NotNull Path path) {
        return resolveDefaultFileResourcePath(UtilsMods.getActiveModId(), path);
    }
}
