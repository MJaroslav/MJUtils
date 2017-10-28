package mjaroslav.mcmods.mjutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.common.CommonProxy;
import mjaroslav.mcmods.mjutils.common.config.Config;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

/**
 * MJUtils mod. Mod-lib for MJaroslav's mods.
 * 
 * @author MJaroslav
 *
 */
@Mod(modid = MJInfo.MODID, name = MJInfo.NAME, version = MJInfo.VERSION, guiFactory = MJInfo.GUIFACTORY)
public class MJUtils {
	public static Logger log = LogManager.getLogger(MJInfo.NAME);

	@Instance(MJInfo.MODID)
	public static MJUtils instance;

	public static Config config = new Config();

	@SidedProxy(clientSide = MJInfo.CLIENTPROXY, serverSide = MJInfo.COMMONPROXY)
	public static CommonProxy proxy = new CommonProxy();

	private static ModInitHandler initHandler = new ModInitHandler(MJInfo.MODID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config.preInit(event);
		this.initHandler.preInit(event);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		config.init(event);
		this.initHandler.init(event);
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		config.postInit(event);
		this.initHandler.postInit(event);
		proxy.postInit(event);
	}

	@EventHandler
	public void constr(FMLConstructionEvent event) {
		this.initHandler.findModules(event);
	}
}
