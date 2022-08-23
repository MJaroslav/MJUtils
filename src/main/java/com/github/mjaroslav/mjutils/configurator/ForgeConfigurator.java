package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.var;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class ForgeConfigurator extends Configurator {
    protected final @NotNull Configuration instance;
    protected @Nullable String version;
    protected final @NotNull String modId;

    @Getter(AccessLevel.NONE)
    protected boolean lazyInitDone;

    public ForgeConfigurator(@NotNull String fileName, @NotNull String modId, @Nullable String version) {
        super(fileName, "cfg");
        this.modId = modId;
        this.version = version;
        instance = new Configuration(getFile().toFile(), getVersion());
        save();
        ConfigurationEventHandler.instance.addConfig(this);
    }

    public abstract void init(@NotNull Configuration instance);

    public ForgeConfigurator(@NotNull String fileName, @NotNull String modId) {
        this(fileName, modId, null);
    }

    @Override
    public void load() {
        instance.load();
        if (!lazyInitDone) {
            init(instance);
            lazyInitDone = true;
        }
        save();
    }

    public void onConfigSaved() {
    }

    @Override
    public void save() {
        if (instance.hasChanged())
            instance.save();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConfigurationEventHandler {
        public static final ConfigurationEventHandler instance = new ConfigurationEventHandler();

        private final List<ForgeConfigurator> list = new ArrayList<>();

        private void addConfig(@NotNull ForgeConfigurator newConfig) {
            for (var config : list)
                if (config.modId.equals(newConfig.modId) && config.getFile().equals(newConfig.getFile()))
                    return;
            list.add(newConfig);
        }

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            for (var config : list)
                if (config.modId.equals(event.modID) && StringUtils.equals(config.getFile().toString(), event.configID)) {
                    config.onConfigSaved();
                    config.save();
                    break;
                }
        }
    }
}
