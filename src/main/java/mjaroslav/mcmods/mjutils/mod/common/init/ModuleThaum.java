package mjaroslav.mcmods.mjutils.mod.common.init;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.*;
import mjaroslav.mcmods.mjutils.handler.ThaumEventHandler;
import mjaroslav.mcmods.mjutils.module.Modular;
import mjaroslav.mcmods.mjutils.module.Module;
import mjaroslav.mcmods.mjutils.util.UtilsThaum;
import mjaroslav.mcmods.mjutils.mod.lib.ModInfo;

@Module(modid = ModInfo.MODID)
public class ModuleThaum implements Modular {
    @Override
    public String name() {
        return "Thaum";
    }

    @Override
    public int priority() {
        return 4;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        UtilsThaum.activate();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ThaumEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    @Override
    public String[] dependencies() {
        return new String[] { "Thaumcraft" };
    }

    @Override
    public boolean canLoad() {
        return true;
    }
}
