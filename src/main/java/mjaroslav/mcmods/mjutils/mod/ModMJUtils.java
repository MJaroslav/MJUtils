package mjaroslav.mcmods.mjutils.mod;

import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.lib.module.*;

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = GUIFACTORY, dependencies = "")
public class ModMJUtils {
    @Instance(MODID)
    public static ModMJUtils instance;

    public static ConfigurationHandler config = new ConfigurationHandler(MODID, LOG);
    @SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
    public static ProxyBase proxy;
    private static ModuleHandler initHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initHandler.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        initHandler.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        initHandler.postInit(event);
    }

    @EventHandler
    public void constr(FMLConstructionEvent event) {
        initHandler = new ModuleHandler(MODID, config, proxy);
        initHandler.findModules(event);
    }
}
