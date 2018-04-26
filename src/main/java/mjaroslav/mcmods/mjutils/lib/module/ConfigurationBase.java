package mjaroslav.mcmods.mjutils.lib.module;

import java.io.File;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraftforge.common.config.Configuration;

/**
 * Base of configuration handler.
 *
 * @author MJaroslav
 */
public abstract class ConfigurationBase implements IInit {
    /**
     * Automatic registration in the configuration update event.
     */
    public ConfigurationBase() {
        ConfigurationEventHandler.addConfig(this);
    }

    /**
     * Instance of configuration file.
     *
     * @return - configuration file.
     */
    public abstract Configuration getInstance();

    /**
     * Set new Configuration instance.
     *
     * @param newConfig
     *            - new configuration file.
     */
    public abstract void setInstance(Configuration newConfig);

    /**
     * Configuration mod id.
     *
     * @return Mod id of configuration.
     */
    public abstract String getModId();

    /**
     * Configuration logger.
     *
     * @return Logger for configuration.
     */
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

    /**
     * In this method, you assign values to your fields from the configuration
     * (instance).
     */
    public abstract void readFields();

    /**
     * Synchronize configuration fields and save in file.
     */
    public final void sync() {
        readFields();
        if (getInstance().hasChanged())
            getInstance().save();
    }

    public static class ConfigurationEventHandler {

        private static ArrayList<ConfigurationBase> list = new ArrayList<ConfigurationBase>();

        public static boolean addConfig(ConfigurationBase newConfig) {
            for (ConfigurationBase config : list)
                if (config.getModId().equals(newConfig.getModId()))
                    return false;
            list.add(newConfig);
            ModInfo.LOG.info("Added configuration for " + newConfig.getModId());
            return true;
        }

        public static ArrayList<ConfigurationBase> getList() {
            return list;
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
