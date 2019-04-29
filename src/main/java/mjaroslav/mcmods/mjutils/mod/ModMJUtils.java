package mjaroslav.mcmods.mjutils.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import mjaroslav.mcmods.mjutils.module.AnnotationBasedConfiguration;
import mjaroslav.mcmods.mjutils.module.ModuleSystem;
import mjaroslav.mcmods.mjutils.module.Proxy;

import static mjaroslav.mcmods.mjutils.mod.lib.ModInfo.*;

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = GUIFACTORY)
public class ModMJUtils {
    @Instance(MODID)
    public static ModMJUtils instance;

    public static AnnotationBasedConfiguration config = new AnnotationBasedConfiguration(MODID, LOG);
    @SidedProxy(clientSide = CLIENTPROXY, serverSide = COMMONPROXY)
    public static Proxy proxy;
    private static ModuleSystem initHandler;

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
        initHandler = new ModuleSystem(MODID, config, proxy);
        initHandler.initSystem(event);
    }
}
