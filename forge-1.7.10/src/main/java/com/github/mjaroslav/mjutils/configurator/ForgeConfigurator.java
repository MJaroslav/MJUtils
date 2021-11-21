package com.github.mjaroslav.mjutils.configurator;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.common.config.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;

public class ForgeConfigurator extends Configurator {
    @Getter
    protected Configuration instance;

    @Nullable
    @Getter
    protected String version;

    @Getter
    @Nonnull
    protected final String modId;

    public ForgeConfigurator(@Nonnull String fileName, @Nonnull String modId, @Nullable String version) {
        super(fileName, "cfg");
        this.modId = modId;
        this.version = version;
        ConfigurationEventHandler.instance.addConfig(this);
    }

    public ForgeConfigurator(@Nonnull String fileName, @Nonnull String modId) {
        this(fileName, modId, null);
    }

    @Override
    public void load() {
        if (instance == null)
            instance = new Configuration(new File(getFile()), getVersion());
        else instance.load();
        sync();
        if(instance.hasChanged())
            save();
    }

    @Override
    public void save() {
        instance.save();
    }

    @Override
    public void sync() {
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ConfigurationEventHandler {
        public static final ConfigurationEventHandler instance = new ConfigurationEventHandler();

        private final ArrayList<ForgeConfigurator> list = new ArrayList<>();

        private void addConfig(ForgeConfigurator newConfig) {
            for (ForgeConfigurator config : list)
                if (config.modId.equals(newConfig.modId) && config.getFile().equals(newConfig.getFile()))
                    return;
            list.add(newConfig);
        }

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            for (ForgeConfigurator config : list)
                if (event.modID.equals(config.modId) && event.configID.equals(config.getFile())) {
                    config.sync();
                    config.save();
                    break;
                }
        }
    }
}
