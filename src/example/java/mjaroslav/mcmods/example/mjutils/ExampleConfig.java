package mjaroslav.mcmods.example.mjutils;

import org.apache.logging.log4j.Logger;

import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase;
import net.minecraftforge.common.config.Configuration;

public class ExampleConfig extends ConfigurationBase {
	private Configuration instance;

	boolean lol = true;

	@Override
	public String getModId() {
		return "mjutilsexample";
	}

	@Override
	public Logger getLogger() {
		return MJUtilsExample.log;
	}

	@Override
	public void readFields() {
		lol = getInstance().getBoolean("lol", "gig", true, "gigigig");
	}

	@Override
	public Configuration getInstance() {
		return this.instance;
	}

	@Override
	public void setInstance(Configuration newConfig) {
		this.instance = newConfig;
	}

}
