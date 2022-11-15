package com.github.mjaroslav.mjutils.config;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgeConfig extends Config {
    protected final Configuration properties;

    public ForgeConfig(@NotNull Path file) {
        this(null, file, null);
    }

    public ForgeConfig(@NotNull String modId, @NotNull Path file) {
        this(modId, file, null);
    }

    public ForgeConfig(@NotNull Path file, @Nullable String version) {
        this(null, file, version);
    }

    public ForgeConfig(@Nullable String modId, @NotNull Path file, @Nullable String version) {
        super(modId, file, version);
        properties = new Configuration(file.toFile(), version);
        registerLoadCallback(this::sync);
        registerSaveCallback(this::sync);
        ForgeConfigEventHandler.INSTANCE.addConfig(this);
    }

    protected void sync() {
    }

    @Override
    protected void setDefault() throws IOException {
        Files.deleteIfExists(getFile());
        loadFile();
    }

    @Override
    protected void loadFile() {
        properties.load();
        if (properties.hasChanged()) saveFile();
    }

    @Override
    protected void saveFile() {
        properties.save();
    }

    @Override
    protected @Nullable String getLoadedVersion() {
        return properties.getLoadedConfigVersion();
    }

    public @NotNull String getConfigId() {
        return getModId() + "@" + getFile();
    }

    @SuppressWarnings("rawtypes")
    public @NotNull List<IConfigElement> getElementList() {
        return new ConfigElement<>(properties.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ForgeConfigEventHandler {
        public static final ForgeConfigEventHandler INSTANCE = new ForgeConfigEventHandler();
        private static final Map<String, ForgeConfig> CONFIGURATIONS = new HashMap<>();

        public void addConfig(@NotNull ForgeConfig config) {
            CONFIGURATIONS.put(config.getConfigId(), config);
        }

        @SubscribeEvent
        public void onConfigChangedEvent(@NotNull ConfigChangedEvent.OnConfigChangedEvent event) {
            val config = CONFIGURATIONS.get(event.configID);
            if (config != null) config.sync();
        }
    }
}
