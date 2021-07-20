package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.event.ConfigChangedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Base configurator interface, you can use any prebuild implementation of this or write your own.
 */
public interface Configurator {
    String PATH_PATTERN = "./config/%s.%s";
    String UNKNOWN_VERSION = "unknown";
    String OLD_VERSION_FILE_EXT = "old";

    /**
     * Configurator unique name, used for getting in {@link ConfiguratorsLoader#getConfigurator(String)}.
     * Also used as {@link ConfigChangedEvent#configID} if realization fires config events.
     * Can be null, if {@link ConfiguratorsLoader} realization don't use {@link ConfiguratorsLoader#getConfigurator(String)}.
     *
     * @return Unique for {@link ConfiguratorsLoader} instance string.
     */
    @Nullable
    String getName();

    /**
     * This should return true if loaded configuration have changes for save.
     * Loaded should save this configuration on next {@link ConfiguratorsLoader#save()} call.
     *
     * @return True if loaded version have any unsaved changes.
     */
    boolean hasChanges();

    /**
     * Configurator owner mod id. Uses in {@link ConfiguratorEvents}.
     *
     * @return Mo id string of configurator owner.
     */
    @Nonnull
    String getModId();

    /**
     * Actual configuration structure version. If it does not match with the {@link Configurator#getLocalVersion()}, the configuration file will be recreated.
     * <br><br>
     * If you write custom realization/loader, you should recreate and reload file on {@link Configurator#load()} if this not match with {@link Configurator#getLocalVersion()} :)
     *
     * @return Unique string for actual configuration structure.
     */
    @Nonnull
    String getActualVersion();

    /**
     * Version of configuration structure from file. If it does not match with the {@link Configurator#getActualVersion()}, the configuration file will be recreated.
     * <br><br>
     * If you write custom realization/loader, you should recreate and reload file on {@link Configurator#load()} if this not match with {@link Configurator#getActualVersion()} :)
     *
     * @return Unique string for current configuration structure.
     */
    @Nonnull
    String getLocalVersion();

    /**
     * String with configuration file in game root dir. Value example: "./config/modid.cfg".
     *
     * @return Path to configuration file.
     */
    @Nonnull
    String getFile();

    /**
     * Try load configuration file. If file not found, create its with default value.
     * <br><br>
     * If you write custom realization/loader, you should use {@link Configurator#getFile()} as path for loading and call {@link Configurator#sync()} after {@link State#OK} load result. If file not exists, create it with default value.
     *
     * @return OK if configuration loaded correctly or ERROR else.
     */
    @Nonnull
    State load();

    /**
     * Try restore default configuration.
     * <br><br>
     * If you write custom realization/loader, this method should just replace current configuration and call {@link Configurator#sync()} after {@link State#OK} result, don't save it to file.
     *
     * @return {@link State#OK} if configuration saved correctly or {@link State#ERROR} else and {@link State#READONLY} or configurator is {@link Configurator#isReadOnly()}.
     */
    @Nonnull
    State restoreDefault();

    /**
     * Try save current configuration to file.
     * <br><br>
     * If you write custom realization/loader, you should use {@link Configurator#getFile()} as path for saving and call this after {@link Configurator#restoreDefault()}
     *
     * @return {@link State#OK} if configuration saved correctly or {@link State#ERROR} else and {@link State#READONLY} or configurator is {@link Configurator#isReadOnly()}.
     */
    @Nonnull
    State save();

    /**
     * Try sync game with new configuration. If any change requires restart game or world, return {@link State#REQUIRES_MC_RESTART}/{@link State#REQUIRES_WORLD_RESTART}.
     *
     * @return {@link State#OK} if configuration synced correctly or {@link State#ERROR} else and {@link State#REQUIRES_MC_RESTART}/{@link State#REQUIRES_WORLD_RESTART} if game or world should be restarted.
     */
    @Nonnull
    State sync();

    /**
     * When enabled, configurator can't use {@link Configurator#save()} and {@link Configurator#restoreDefault()}.
     * <br><br>
     * If you write custom realization/loader, you should just return {@link State#READONLY} in {@link Configurator#save()} and {@link Configurator#restoreDefault()}.
     *
     * @return True for disable writing.
     */
    boolean isReadOnly();

    /**
     * When enabled, configurator will crash game on any {@link State#ERROR} return.
     * <br><br>
     * If you write custom realization/loader, you should crash the game on any ERROR return when this is true :) You can also crash on {@link State#UNKNOWN} too.
     * @return True for error crashing.
     */
    boolean canCrashOnError();

    /**
     * States for {@link Configurator} methods.
     * <br><br>
     * Use {@link State#UNKNOWN} as placeholder for unknown results. Can be used as {@link State#ERROR}.
     * <br>
     * Use {@link State#ERROR} when any exception throws in methods. Can crash game if {@link Configurator#canCrashOnError()} is true.
     * <br>
     * Use {@link State#OK} for any correct method results.
     * <br>
     * Use {@link State#REQUIRES_MC_RESTART} if game requires restart after {@link Configurator#sync() syncing}.
     * <br>
     * Use {@link State#REQUIRES_WORLD_RESTART} if world requires restart after {@link Configurator#sync() syncing}.
     */
    enum State {
        UNKNOWN, ERROR, OK, READONLY, REQUIRES_MC_RESTART, REQUIRES_WORLD_RESTART;

        public boolean isNotCool() {
            return this == UNKNOWN || this == OK;
        }
    }
}
