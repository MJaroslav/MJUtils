package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public abstract class FileBasedConfiguration implements Initializator {
    protected Configuration instance;
    public final String modID;
    protected final Logger logger;

    public FileBasedConfiguration(String modID, Logger logger) {
        this.modID = modID;
        this.logger = logger;
        ConfigurationEventHandler.instance.addConfig(this);
    }

    public abstract void readFields();

    public Configuration getInstance() {
        return instance;
    }

    public void setInstance(Configuration newConfig) {
        instance = newConfig;
    }

    @Override
    public final void preInit(FMLPreInitializationEvent event) {
        if (getInstance() == null)
            setInstance(new Configuration(event.getSuggestedConfigurationFile()));
        try {
            getInstance().load();
        } catch (Exception e) {
            logger.error("Unable to load configuration!");
        } finally {
            if (getInstance().hasChanged())
                getInstance().save();
        }
        sync();
    }

    private void sync() {
        readFields();
        if (getInstance().hasChanged())
            getInstance().save();
    }

    public static class ConfigurationEventHandler {
        public static final ConfigurationEventHandler instance = new ConfigurationEventHandler();

        private ConfigurationEventHandler() {
        }

        private final ArrayList<FileBasedConfiguration> list = new ArrayList<>();

        private void addConfig(FileBasedConfiguration newConfig) {
            for (FileBasedConfiguration config : list)
                if (config.modID.equals(newConfig.modID))
                    return;
            list.add(newConfig);
            ModInfo.LOG.info(String.format("Added configuration file for \"%s\" modID.", newConfig.modID));
        }

        @SubscribeEvent
        public void onConfigChanged(OnConfigChangedEvent event) {
            for (FileBasedConfiguration config : list)
                if (event.modID.equals(config.modID)) {
                    config.sync();
                    config.logger.info(String.format("Configuration synchronized for \"%s\" modID.", config.modID));
                    break;
                }
        }
    }
}
