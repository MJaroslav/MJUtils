package io.github.mjaroslav.mjutils.config;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.util.object.game.config.FileAsCategoryElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static io.github.mjaroslav.mjutils.lib.MJUtilsInfo.*;

/**
 * {@link Config} implementation for forge {@link Configuration} with manually property handling by sync callbacks.
 *
 * @see Configuration
 * @see ForgeConfig#registerSyncCallback(Runnable)
 */
@Getter
public class ForgeConfig extends Config {
    protected final Set<Runnable> syncCallbacks = new LinkedHashSet<>();
    protected final Configuration properties;

    /**
     * @see Config#Config(String, Path, String) Super constructor for parameters.
     */
    public ForgeConfig(@NotNull Path file) {
        this(null, file, null);
    }

    /**
     * @see Config#Config(String, Path, String) Super constructor for parameters.
     */
    public ForgeConfig(@NotNull String modId, @NotNull Path file) {
        this(modId, file, null);
    }

    /**
     * @see Config#Config(String, Path, String) Super constructor for parameters.
     */
    public ForgeConfig(@NotNull Path file, @Nullable String version) {
        this(null, file, version);
    }

    /**
     * @see Config#Config(String, Path, String) Super constructor for parameters.
     */
    public ForgeConfig(@Nullable String modId, @NotNull Path file, @Nullable String version) {
        super(modId, file, version);
        properties = new Configuration(file.toFile(), version);
        registerLoadCallback(this::sync);
        registerSaveCallback(this::sync);
        ForgeConfigEventHandler.INSTANCE.addConfig(this);
    }

    /**
     * Adds extra logic after each config sync (after save, load and editing in GUI).
     *
     * @param callback functional interface with logic.
     * @return true if this config did not already contain the specified callback.
     * @see ForgeConfig#unregisterSyncCallback(Runnable)
     */
    public boolean registerSyncCallback(@NotNull Runnable callback) {
        return syncCallbacks.add(callback);
    }

    /**
     * Adds extra logic after each config sync (after save, load and editing in GUI).
     *
     * @param callback already registered callback for unregister.
     * @return true if this config contained the specified callback.
     * @see ForgeConfig#registerSyncCallback(Runnable)
     */
    public boolean unregisterSyncCallback(@NotNull Runnable callback) {
        return syncCallbacks.remove(callback);
    }

    /**
     * Synchronize properties with loaded or save values to config file. You also can add extra logic by sync callbacks.
     *
     * @see ForgeConfig#registerSyncCallback(Runnable)
     */
    public final void sync() {
        syncCallbacks.forEach(Runnable::run);
        if (properties.hasChanged()) properties.save();
    }

    @Override
    protected void restoreDefaultFile() throws IOException {
        Files.deleteIfExists(getFile());
        properties.load();
        sync();
    }

    @Override
    protected void loadFile() {
        properties.load();
    }

    @Override
    protected void saveFile() {
        properties.save();
    }

    @Override
    protected @Nullable String getLoadedVersion() {
        return properties.getLoadedConfigVersion();
    }

    /**
     * ID for config comparing on {@link OnConfigChangedEvent} called. Use it in GUIConfig.
     *
     * @return just pair of internal ID and file.
     */
    public @NotNull String getConfigId() {
        return getModId() + "@" + getFile();
    }

    /**
     * You can use it for collecting root category (with name "general") to {@link IConfigElement} list for GUIConfig.
     *
     * @return list of general category property and subcategories.
     */
    @SuppressWarnings("rawtypes")
    public @NotNull List<IConfigElement> getElementList() {
        return getElementList(Configuration.CATEGORY_GENERAL);
    }

    @SuppressWarnings("rawtypes")
    public @NotNull List<IConfigElement> getElementList(@NotNull String rootCategory) {
        return new ConfigElement<>(properties.getCategory(rootCategory)).getChildElements();
    }

    public @NotNull FileAsCategoryElement getCategoryElement() {
        return getCategoryElement(Configuration.CATEGORY_GENERAL);
    }

    public @NotNull FileAsCategoryElement getCategoryElement(@NotNull String rootCategory) {
        return new FileAsCategoryElement(properties, rootCategory, getConfigId());
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ForgeConfigEventHandler {
        public static final ForgeConfigEventHandler INSTANCE = new ForgeConfigEventHandler();

        private final Map<String, ForgeConfig> CONFIGURATIONS = new HashMap<>();

        private void addConfig(@NotNull ForgeConfig config) {
            CONFIGURATIONS.put(config.getConfigId(), config);
        }

        @SubscribeEvent
        public void onConfigChangedEvent(@NotNull OnConfigChangedEvent event) {
            CONFIGURATIONS.entrySet().stream().filter(entry -> entry.getKey().startsWith(event.configID)
                && StringUtils.equals(entry.getValue().getModId(), event.modID)).forEach(entry -> {
                entry.getValue().sync();
                LOG_LIB.info("Configuration {} synced", event.configID);
            });
        }
    }
}
