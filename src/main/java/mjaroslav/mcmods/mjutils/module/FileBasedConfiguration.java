package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Standard forge configuration. It is saved in the
 * configurations directory in the {@link FileBasedConfiguration#modID}.cfg file.
 * You can use it by yourself or with {@link ModuleSystem}.
 *
 * @see AnnotationBasedConfiguration
 * @see ModuleSystem
 */
// TODO: Написать afterSync интерфейс для действий пользователя после обновления конфигурации.
public abstract class FileBasedConfiguration implements Initializator {
    protected Configuration instance;
    /**
     * ModID of owner-modification. Used as name of
     * configuration file as well as used in update
     * event. Should not be null.
     */
    public final String modID;
    /**
     * logger for configuration. Should not be null.
     * You can use your mod logger.
     */
    protected final Logger logger;

    /**
     * See class documentation.
     *
     * @param modID  see {@link FileBasedConfiguration#modID}
     * @param logger see {@link FileBasedConfiguration#logger}
     */
    public FileBasedConfiguration(String modID, Logger logger) {
        this.modID = modID;
        this.logger = logger;
        ConfigurationEventHandler.instance.addConfig(this);
    }

    /**
     * Retrieving values from a configuration object
     * is done only in this method.
     *
     * @param instance configuration object.
     */
    public abstract void readFields(Configuration instance);

    /**
     * Get configuration object.
     *
     * @return Null if before PreInitialization or instance after.
     */
    public Configuration getInstance() {
        return instance;
    }

    /**
     * Set configuration object. Use at your own risk
     *
     * @param newConfig new object.
     */
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
            logger.error(String.format("Unable to load configuration for \"%s\"!", modID));
        } finally {
            if (getInstance().hasChanged())
                getInstance().save();
        }
        sync();
    }

    private void sync() {
        readFields(getInstance());
        if (getInstance().hasChanged())
            getInstance().save();
        // Place for afterSync
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
            ModInfo.LOG.info(String.format("Added configuration file for \"%s\".", newConfig.modID));
        }

        @SubscribeEvent
        public void onConfigChanged(OnConfigChangedEvent event) {
            for (FileBasedConfiguration config : list)
                if (event.modID.equals(config.modID)) {
                    config.sync();
                    config.logger.info(String.format("Configuration synchronized for \"%s\".", config.modID));
                    break;
                }
        }
    }
}
