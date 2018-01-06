package mjaroslav.mcmods.mjutils;

import static mjaroslav.mcmods.mjutils.MJInfo.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.common.MJUtilsCommonProxy;
import mjaroslav.mcmods.mjutils.common.config.MJUtilsConfig;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = GUIFACTORY, dependencies = "")
public class MJUtils {
	@Instance(MJInfo.MODID)
	public static MJUtils instance;

	public static MJUtilsConfig config = new MJUtilsConfig();
	@SidedProxy(clientSide = MJInfo.CLIENTPROXY, serverSide = MJInfo.COMMONPROXY)
	public static MJUtilsCommonProxy proxy = new MJUtilsCommonProxy();
	private static ModInitHandler initHandler = new ModInitHandler(MJInfo.MODID, config, proxy);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initHandler.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new ConfigurationEventHandler());
		initHandler.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		initHandler.postInit(event);
	}

	@EventHandler
	public void constr(FMLConstructionEvent event) {
		initHandler.findModules(event);
	}
}
