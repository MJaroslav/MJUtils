package mjaroslav.mcmods.mjutils;

import static mjaroslav.mcmods.mjutils.lib.ModInfo.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.common.CommonProxy;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationBase.ConfigurationEventHandler;
import mjaroslav.mcmods.mjutils.common.objects.ConfigurationHandler;
import mjaroslav.mcmods.mjutils.common.objects.ModInitHandler;
import mjaroslav.mcmods.mjutils.lib.ConfigInfo;

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = GUIFACTORY, dependencies = "")
public class ModMJUtils {
    @Instance(MODID)
    public static ModMJUtils instance;

    public static ConfigurationHandler config = new ConfigurationHandler(MODID, LOG, ConfigInfo.class);
    @SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
    public static CommonProxy proxy = new CommonProxy();
    private static ModInitHandler initHandler = new ModInitHandler(MODID, config, proxy);

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
