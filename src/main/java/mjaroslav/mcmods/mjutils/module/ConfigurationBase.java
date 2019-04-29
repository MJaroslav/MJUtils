package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

public abstract class ConfigurationBase implements Initializator {
    public ConfigurationBase() {
        ConfigurationEventHandler.instance.addConfig(this);
    }

    public abstract Configuration getInstance();

    public abstract void setInstance(Configuration newConfig);

    public abstract String getModId();

    public abstract Logger getLogger();

    @Override
    public final void preInit(FMLPreInitializationEvent event) {
        if (getInstance() == null)
            setInstance(new Configuration(new File(event.getModConfigurationDirectory() + "/" + getModId() + ".cfg")));
        try {
            getInstance().load();
        } catch (Exception e) {
            this.getLogger().error("Unable to load configuration!");
        } finally {
            if (getInstance().hasChanged()) {
                getInstance().save();
            }
        }
        sync();
    }

    @Override
    public final void init(FMLInitializationEvent event) {
    }

    @Override
    public final void postInit(FMLPostInitializationEvent event) {
    }

    public abstract void readFields();

    private void sync() {
        readFields();
        if (getInstance().hasChanged())
            getInstance().save();
    }

    public static class ConfigurationEventHandler {
        public static final ConfigurationEventHandler instance = new ConfigurationEventHandler();

        private ConfigurationEventHandler() {}

        private final ArrayList<ConfigurationBase> list = new ArrayList<>();

        private void addConfig(ConfigurationBase newConfig) {
            for (ConfigurationBase config : list)
                if (config.getModId().equals(newConfig.getModId()))
                    return;
            list.add(newConfig);
            ModInfo.LOG.info("Added configuration for " + newConfig.getModId());
        }

        @SubscribeEvent
        public void onConfigChanged(OnConfigChangedEvent event) {
            for (ConfigurationBase config : list)
                if (event.modID.equals(config.getModId())) {
                    config.sync();
                    config.getLogger().info("Configuration reloaded");
                }
        }
    }
}
