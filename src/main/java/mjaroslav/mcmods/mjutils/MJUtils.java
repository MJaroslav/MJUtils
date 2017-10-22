package mjaroslav.mcmods.mjutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.objects.InitHandler;

@Mod(modid = MJInfo.MODID, name = MJInfo.NAME, version = MJInfo.VERSION)
public class MJUtils {
	public static Logger log = LogManager.getLogger(MJInfo.NAME);
	private static InitHandler initHandler = new InitHandler(
			MJUtils.class.getProtectionDomain().getCodeSource().getLocation(), MJInfo.MODID, MJInfo.MODULEPACKAGE, log,
			true);

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
