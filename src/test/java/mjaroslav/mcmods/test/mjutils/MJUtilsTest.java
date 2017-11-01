package mjaroslav.mcmods.test.mjutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

@Mod(name = "MJUtilsTest", modid = "mjutilstest", version = "1.7.10-1", guiFactory = "mjaroslav.mcmods.test.mjutils.TestGuiFactory")
public class MJUtilsTest {
	public static Logger log = LogManager.getLogger("MJUtilsTest");
	private static ModInitHandler initHandler = new ModInitHandler("mjutilstest");
	public static TestConfig config = new TestConfig();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.config.preInit(event);
		this.initHandler.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		this.config.init(event);
		this.initHandler.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.config.postInit(event);
		this.initHandler.postInit(event);
	}

	@EventHandler
	public void constr(FMLConstructionEvent event) {
		this.initHandler.findModules(event);
	}
}
