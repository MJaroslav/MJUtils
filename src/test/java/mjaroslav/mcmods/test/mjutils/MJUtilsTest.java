package mjaroslav.mcmods.test.mjutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.objects.InitHandler;

@Mod(name = "MJUtilsTest", modid = "mjutilstest", version = "1.7.10-1", useMetadata = false)
public class MJUtilsTest {
	public static Logger log = LogManager.getLogger("MJUtilsTest");
	private static InitHandler initHandler = new InitHandler(
			MJUtilsTest.class.getProtectionDomain().getCodeSource().getLocation(), "mjutilstest",
			"mjaroslav.mcmods.test.mjutils", log, true);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.initHandler.findModules();
		this.initHandler.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.initHandler.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.initHandler.postInit(event);
	}
}
