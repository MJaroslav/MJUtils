package mjaroslav.mcmods.mjutils.common.objects;

import java.io.File;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

/**
 * Base of configuration handler.
 * 
 * @author MJaroslav
 *
 */
public abstract class ConfigurationBase implements IModModule {
	/** Instance of configuration file. */
	public Configuration instance;
	/** Modification id of handler. */
	public String modid;
	/** Logger of handler. */
	private Logger logger;

	public ConfigurationBase(String modid, Logger logger) {
		this.modid = modid;
		this.logger = logger;
	}

	@Override
	public final String getModuleName() {
		return "Config";
	}

	@Override
	public final void preInit(FMLPreInitializationEvent event) {
		if (instance == null)
			instance = new Configuration(new File(event.getModConfigurationDirectory() + "/" + this.modid + ".cfg"));
		try {
			instance.load();
		} catch (Exception e) {
			logger.error("Unable to load configuration!");
		} finally {
			if (instance.hasChanged()) {
				instance.save();
			}
		}
		sync();
		FMLCommonHandler.instance().bus().register(this);
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

	private final void sync() {
		readFields();
		if (instance.hasChanged())
			instance.save();
	}

	@SubscribeEvent
	public final void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(this.modid)) {
			sync();
			logger.info("Configuration reloaded");
		}
	}
}
