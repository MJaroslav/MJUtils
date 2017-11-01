package mjaroslav.mcmods.test.mjutils;

import org.apache.logging.log4j.Logger;

import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase;
import net.minecraftforge.common.config.Configuration;

public class TestConfig extends ConfigurationBase {
	private Configuration instance;

	boolean lol = true;

	@Override
	public String getModId() {
		return "mjutilstest";
	}

	@Override
	public Logger getLogger() {
		return MJUtilsTest.log;
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
