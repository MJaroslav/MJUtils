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

public abstract class ConfigurationBase implements IModModule {
	public Configuration config;
	public String modid;
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
		if (config == null)
			config = new Configuration(new File(event.getModConfigurationDirectory() + "/" + this.modid + ".cfg"));
		try {
			config.load();
		} catch (Exception e) {
			logger.error("Unable to load configuration!");
		} finally {
			if (config.hasChanged()) {
				config.save();
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

	public abstract void readFields();

	private final void sync() {
		readFields();
		if (config.hasChanged())
			config.save();
	}

	@SubscribeEvent
	public final void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(this.modid)) {
			sync();
			logger.info("Configuration reloaded");
		}
	}
}
